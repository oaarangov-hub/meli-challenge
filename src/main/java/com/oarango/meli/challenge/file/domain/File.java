package com.oarango.meli.challenge.file.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class File {

    private String site;
    private long id;

    public String generateItemKey() {
        return site.concat(String.valueOf(id));
    }
}
