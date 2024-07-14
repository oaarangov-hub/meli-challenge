package com.oarango.meli.challenge.configuration.application;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfig {
    @Value("${meli.base-url}")
    private String MELI_API_URL;

    @Bean
    public WebClient webClient() {
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxConnections(500)
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();
        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
                .responseTimeout(Duration.ofSeconds(90))
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .keepAlive(true)
                .protocol(HttpProtocol.HTTP11, HttpProtocol.H2)
                .compress(true)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(90, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(90, TimeUnit.SECONDS)));
        return WebClient.builder()
                .baseUrl(MELI_API_URL)
                .filter((request, next) -> {
                    log.info("Making request to: {}", request.url());
                    return next.exchange(request);
                })
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ApplicationRunner warmUpRunner(WarmUpService warmUpService) {
        return args -> warmUpService.warmUp();
    }
}
