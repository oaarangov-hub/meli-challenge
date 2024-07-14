package com.oarango.meli.challenge.item.infrastructure.repository;

import com.oarango.meli.challenge.item.domain.Item;
import com.oarango.meli.challenge.item.domain.ItemGateway;
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
public class ItemRepository implements ItemGateway {
    private static final String ATTRIBUTES = "price,start_time,category_id,currency_id,seller_id";
    private final WebClient webClient;

    @Override
    @Cacheable("externalData")
    public Mono<Item> findItemByKey(String itemKey) {
        if (itemKey == null || itemKey.isEmpty()) {
            return Mono.just(Item.builder().build());
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/items/{itemKey}")
                        .queryParam("attributes", ATTRIBUTES)
                        .build(itemKey))
                .retrieve()
                .bodyToMono(ItemApi.class)
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(ItemApi.builder().build()))
                .onErrorResume(TimeoutException.class, ex -> Mono.just(ItemApi.builder().build()))
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.just(ItemApi.builder().build()))
                .timeout(Duration.ofSeconds(90))
                .doOnCancel(() -> log.warn("Request cancelled for itemKey: {}", itemKey))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(30))
                        .doAfterRetry(signal -> log.info("Retrying request for itemKey: {}", itemKey)))
                .map(ItemApi::toDomain);
    }
}
