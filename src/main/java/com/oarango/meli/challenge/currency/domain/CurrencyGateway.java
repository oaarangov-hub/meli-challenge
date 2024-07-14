package com.oarango.meli.challenge.currency.domain;

import reactor.core.publisher.Mono;

public interface CurrencyGateway {
    Mono<Currency> findCurrencyById(String currencyId);
}
