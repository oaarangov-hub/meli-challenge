package com.oarango.meli.challenge.file.domain;

import com.oarango.meli.challenge.item.domain.ItemProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FileHandler {
    @Value("${file.content-type}")
    private String FILE_CONTENT_TYPE;
    private final ItemProcessor itemProcessor;
    private final FileReader fileReader;

    public Mono<ServerResponse> proccessFile(ServerRequest request) {
        return request.multipartData().flatMap(multipartData -> {
            FilePart filePart = (FilePart) multipartData.getFirst("file");

            if (filePart == null) {
                return ServerResponse.badRequest().bodyValue("No se encontró el archivo CSV");
            }

            if (!FILE_CONTENT_TYPE.equals(Objects.requireNonNull(filePart.headers().getContentType()).toString())) {
                return ServerResponse.badRequest()
                        .bodyValue("Tipo de archivo no permitido. Por favor, suba un archivo " + FILE_CONTENT_TYPE);
            }

            return filePart.content()
                    .map(dataBuffer -> fileReader.readFile(dataBuffer.asInputStream()))
                    .flatMap(itemProcessor::processItems)
                    .then(ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("File processed successfully"));
        });
    }
}
