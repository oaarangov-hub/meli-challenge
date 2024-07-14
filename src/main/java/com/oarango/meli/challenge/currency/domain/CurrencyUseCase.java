package com.oarango.meli.challenge.currency.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyUseCase {
    private final CurrencyGateway currencyGateway;

    public Mono<Currency> getCurrencyById(String currencyId) {
        return currencyGateway.findCurrencyById(currencyId);
    }
}
