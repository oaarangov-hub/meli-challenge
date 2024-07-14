package com.oarango.meli.challenge.item.domain;

import com.oarango.meli.challenge.category.domain.CategoryUseCase;
import com.oarango.meli.challenge.currency.domain.CurrencyUseCase;
import com.oarango.meli.challenge.file.domain.File;
import com.oarango.meli.challenge.records.domain.Record;
import com.oarango.meli.challenge.records.domain.RecordUseCase;
import com.oarango.meli.challenge.user.domain.UserUseCase;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemProcessor {
    private final ItemUseCase itemUseCase;
    private final CategoryUseCase categoryUseCase;
    private final CurrencyUseCase currencyUseCase;
    private final UserUseCase userUseCase;
    private final RecordUseCase recordUseCase;
    private final CircuitBreaker circuitBreaker;

    public Mono<List<Item>> processItems(List<File> lines) {
        log.info("Process {} items", lines.size());
        int concurrency = 50;
        return Flux.fromIterable(lines)
                .onBackpressureBuffer(2000)
                .parallel(concurrency)
                .runOn(Schedulers.boundedElastic())
                .flatMap(this::processItem)
                .flatMap(this::saveRecord)
                .sequential()
                .collectList()
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
    }

    private Mono<Item> processItem(File line) {
        log.info("Process item {}", line);
        return itemUseCase.getItemByKey(line.generateItemKey())
                .filter(Objects::nonNull)
                .flatMap(this::fetchCategory)
                .flatMap(this::fetchCurrency)
                .flatMap(this::fetchUser)
                .flatMap(item -> {
                    item.setId(line.getId());
                    item.setSite(line.getSite());
                    return Mono.just(item);
                })
                .timeout(Duration.ofSeconds(30))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10)))
                .onErrorResume(this::handleError);
    }

    private Mono<Item> fetchCategory(Item item) {
        return categoryUseCase.getCategoryById(item.getCategoryId())
                .doOnNext(item::setCategory)
                .thenReturn(item);
    }

    private Mono<Item> fetchCurrency(Item item) {
        return currencyUseCase.getCurrencyById(item.getCurrencyId())
                .doOnNext(item::setCurrency)
                .thenReturn(item);
    }

    private Mono<Item> fetchUser(Item item) {
        return userUseCase.getUserById(item.getSellerId())
                .doOnNext(item::setUser)
                .thenReturn(item);
    }

    private Mono<Item> saveRecord(Item item) {
        Record record = Item.toRecord(item);
        return recordUseCase.save(record).thenReturn(item);
    }

    private Mono<Item> handleError(Throwable error) {
        if (error instanceof ReadTimeoutException) {
            log.error("ReadTimeoutException occurred: {}", error.getMessage());
        } else if (error instanceof TimeoutException) {
            log.error("TimeoutException occurred: {}", error.getMessage());
        }
        return Mono.empty();
    }
}
