package com.oarango.meli.challenge.records.domain;

import com.oarango.meli.challenge.records.infrastructure.repository.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    private Long itemId;
    private String site;
    private String startTime;
    private BigDecimal price;
    private String categoryName;
    private String sellerNickname;
    private String currencyDescription;

    public static RecordEntity toEntity(Record record) {
        return RecordEntity.builder()
                .item_id(record.getItemId())
                .site(record.getSite())
                .start_time(record.getStartTime())
                .price(record.getPrice())
                .category_name(record.getCategoryName())
                .seller_nickname(record.getSellerNickname())
                .currency_description(record.getCurrencyDescription())
                .build();
    }
}
