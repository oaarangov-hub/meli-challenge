package com.oarango.meli.challenge.category.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryUseCase {
    private final CategoryGateway categoryGateway;

    public Mono<Category> getCategoryById(String categoryId) {
        return categoryGateway.findCategory(categoryId);
    }
}
