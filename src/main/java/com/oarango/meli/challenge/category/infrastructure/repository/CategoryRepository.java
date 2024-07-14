package com.oarango.meli.challenge.category.infrastructure.repository;

import com.oarango.meli.challenge.category.domain.Category;
import com.oarango.meli.challenge.category.domain.CategoryGateway;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryRepository implements CategoryGateway {
    private static final String ATTRIBUTES = "name";
    private final WebClient webClient;

    @Override
    @Cacheable("externalData")
    public Mono<Category> findCategory(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return Mono.just(Category.builder().build());
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/categories/{categoryId}")
                        .queryParam("attributes", ATTRIBUTES)
                        .build(categoryId))
                .retrieve()
                .bodyToMono(CategoryApi.class)
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(CategoryApi.builder().build()))
                .onErrorResume(TimeoutException.class, ex -> Mono.just(CategoryApi.builder().build()))
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.just(CategoryApi.builder().build()))
                .timeout(Duration.ofSeconds(90))
                .doOnCancel(() -> log.warn("Request cancelled for categoryId: {}", categoryId))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(30))
                        .doAfterRetry(signal -> log.info("Retrying request for categoryId: {}", categoryId)))
                .map(CategoryApi::toDomain);
    }
}
