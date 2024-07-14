package com.oarango.meli.challenge.file.infrastructure.api;

import com.oarango.meli.challenge.file.domain.FileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<ServerResponse> routes(FileHandler handler) {
        return RouterFunctions.route(RequestPredicates.POST("/api/file/upload"), handler::proccessFile);
    }
}
