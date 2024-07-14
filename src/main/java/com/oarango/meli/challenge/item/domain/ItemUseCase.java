package com.oarango.meli.challenge.item.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ItemUseCase {
    private final ItemGateway itemGateway;

    public Mono<Item> getItemByKey(String itemKey) {
        return itemGateway.findItemByKey(itemKey);
    }
}
