package com.oarango.meli.challenge.configuration.application;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WarmUpService {
    private final WebClient webClient;

    @PostConstruct
    public void warmUp() {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sites/MCO")
                        .queryParam("attributes", "name")
                        .build())
                .retrieve()
                .bodyToMono(Site.class)
                .subscribe();
    }

    @Getter
    @Setter
    static class Site {
        private String name;
    }
}
