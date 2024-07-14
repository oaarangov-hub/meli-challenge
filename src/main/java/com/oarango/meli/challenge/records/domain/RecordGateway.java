package com.oarango.meli.challenge.records.domain;

import reactor.core.publisher.Mono;

public interface RecordGateway {
    Mono<Record> save(Record record);
}
