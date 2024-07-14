package com.oarango.meli.challenge.records.infrastructure.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PsqlRecordRepository extends R2dbcRepository<RecordEntity, Integer> {
}
