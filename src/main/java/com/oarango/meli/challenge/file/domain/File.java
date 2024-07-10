package com.oarango.meli.challenge.file.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class File {

    private String site;
    private long id;

    public String generateItemKey() {
        return site.concat(String.valueOf(id));
    }
}
