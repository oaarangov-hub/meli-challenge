package com.oarango.meli.challenge.user.infrastructure.repository;

import com.oarango.meli.challenge.user.domain.User;
import com.oarango.meli.challenge.user.domain.UserGateway;
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
public class UserRepository implements UserGateway {
    private static final String ATTRIBUTES = "nickname";
    private final WebClient webClient;

    @Override
    @Cacheable("externalData")
    public Mono<User> findUserById(Long sellerId) {
        if (sellerId == null) {
            return Mono.just(User.builder().build());
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/{sellerId}")
                        .queryParam("attributes", ATTRIBUTES)
                        .build(sellerId))
                .retrieve()
                .bodyToMono(UserApi.class)
                .cache()
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(UserApi.builder().build()))
                .onErrorResume(TimeoutException.class, ex -> Mono.just(UserApi.builder().build()))
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.just(UserApi.builder().build()))
                .timeout(Duration.ofSeconds(90))
                .doOnCancel(() -> log.warn("Request cancelled for sellerId: {}", sellerId))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(30))
                        .doAfterRetry(signal -> log.info("Retrying request for sellerId: {}", sellerId)))
                .map(UserApi::toDomain);
    }
}
