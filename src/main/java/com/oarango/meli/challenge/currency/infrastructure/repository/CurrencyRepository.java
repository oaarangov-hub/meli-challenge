package com.oarango.meli.challenge.currency.infrastructure.repository;

import com.oarango.meli.challenge.currency.domain.Currency;
import com.oarango.meli.challenge.currency.domain.CurrencyGateway;
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
public class CurrencyRepository implements CurrencyGateway {
    private static final String ATTRIBUTES = "description";
    private final WebClient webClient;

    @Override
    @Cacheable("externalData")
    public Mono<Currency> findCurrencyById(String currencyId) {
        if (currencyId == null || currencyId.isEmpty()) {
            return Mono.just(Currency.builder().build());
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/currencies/{currencyId}")
                        .queryParam("attributes", ATTRIBUTES)
                        .build(currencyId))
                .retrieve()
                .bodyToMono(CurrencyApi.class)
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(CurrencyApi.builder().build()))
                .onErrorResume(TimeoutException.class, ex -> Mono.just(CurrencyApi.builder().build()))
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.just(CurrencyApi.builder().build()))
                .timeout(Duration.ofSeconds(90))
                .doOnCancel(() -> log.warn("Request cancelled for currencyId: {}", currencyId))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(30))
                        .doAfterRetry(signal -> log.info("Retrying request for currencyId: {}", currencyId)))
                .map(CurrencyApi::toDomain);
    }
}
