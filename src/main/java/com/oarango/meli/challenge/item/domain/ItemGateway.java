package com.oarango.meli.challenge.item.domain;

import reactor.core.publisher.Mono;

public interface ItemGateway {
    Mono<Item> findItemByKey(String itemKey);
}
