package com.oarango.meli.challenge.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserGateway userGateway;

    public Mono<User> getUserById(Long sellerId) {
        return userGateway.findUserById(sellerId);
    }
}
