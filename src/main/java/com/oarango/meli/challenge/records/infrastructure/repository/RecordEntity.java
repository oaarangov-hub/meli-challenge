package com.oarango.meli.challenge.records.infrastructure.repository;

import com.oarango.meli.challenge.records.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("records")
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordEntity {
    @Id
    private Integer id;
    private String site;
    private Long item_id;
    private String start_time;
    private BigDecimal price;
    private String category_name;
    private String seller_nickname;
    private String currency_description;

    public static Record toDomain(RecordEntity recordEntity) {
        return Record.builder()
                .itemId(recordEntity.getItem_id())
                .site(recordEntity.getSite())
                .startTime(recordEntity.getStart_time())
                .price(recordEntity.getPrice())
                .categoryName(recordEntity.getCategory_name())
                .sellerNickname(recordEntity.getSeller_nickname())
                .currencyDescription(recordEntity.getCurrency_description())
                .build();
    }
}
