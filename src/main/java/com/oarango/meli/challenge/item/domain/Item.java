package com.oarango.meli.challenge.item.domain;

import com.oarango.meli.challenge.category.domain.Category;
import com.oarango.meli.challenge.currency.domain.Currency;
import com.oarango.meli.challenge.records.domain.Record;
import com.oarango.meli.challenge.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Item {
    private String site;
    private Long id;

    private BigDecimal price;
    private String start_time;
    private String categoryId;
    private String currencyId;
    private Long sellerId;

    private Category category;
    private Currency currency;
    private User user;

    public static Record toRecord(Item item) {
        return Record.builder()
                .itemId(item.getId())
                .site(item.getSite())
                .startTime(item.getStart_time())
                .price(item.getPrice())
                .categoryName(item.getCategory().getName())
                .sellerNickname(item.getUser().getNickname())
                .currencyDescription(item.getCurrency().getDescription())
                .build();
    }
}
