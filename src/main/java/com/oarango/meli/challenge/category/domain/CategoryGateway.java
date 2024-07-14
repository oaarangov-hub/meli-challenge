package com.oarango.meli.challenge.category.domain;

import reactor.core.publisher.Mono;

public interface CategoryGateway {
    Mono<Category> findCategory(String categoryId);
}
