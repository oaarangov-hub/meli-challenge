package com.oarango.meli.challenge.item.infrastructure.repository;

import com.oarango.meli.challenge.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemApi {
    private BigDecimal price;
    private String category_id;
    private Long seller_id;
    private String currency_id;

    public static Item toDomain(ItemApi itemApi) {
        return Item.builder()
                .price(itemApi.getPrice())
                .categoryId(itemApi.getCategory_id())
                .sellerId(itemApi.getSeller_id())
                .currencyId(itemApi.getCurrency_id())
                .build();
    }
}
