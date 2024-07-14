package com.oarango.meli.challenge.records.infrastructure.repository;

import com.oarango.meli.challenge.records.domain.Record;
import com.oarango.meli.challenge.records.domain.RecordGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordGateway {
    private final PsqlRecordRepository psqlRecordRepository;

    @Override
    public Mono<Record> save(Record record) {
        return psqlRecordRepository.save(Record.toEntity(record)).map(RecordEntity::toDomain);
    }
}
