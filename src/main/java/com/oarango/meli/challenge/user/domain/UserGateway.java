package com.oarango.meli.challenge.user.domain;

import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<User> findUserById(Long sellerId);
}
