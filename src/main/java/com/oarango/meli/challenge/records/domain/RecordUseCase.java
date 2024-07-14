package com.oarango.meli.challenge.records.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RecordUseCase {
    private final RecordGateway recordGateway;

    public Mono<Void> save(Record record) {
        return recordGateway.save(record).then();
    }
}
