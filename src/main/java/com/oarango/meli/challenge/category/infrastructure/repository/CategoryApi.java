package com.oarango.meli.challenge.category.infrastructure.repository;

import com.oarango.meli.challenge.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryApi {
    private String name;

    public static Category toDomain(CategoryApi categoryApi) {
        return Category.builder()
                .name(categoryApi.getName())
                .build();
    }
}
