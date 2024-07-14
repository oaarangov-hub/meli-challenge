package com.oarango.meli.challenge.currency.infrastructure.repository;

import com.oarango.meli.challenge.currency.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyApi {
    private String description;

    public static Currency toDomain(CurrencyApi currencyApi) {
        return Currency.builder()
                .description(currencyApi.getDescription())
                .build();
    }
}
