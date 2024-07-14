package com.oarango.meli.challenge.file.infrastructure.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oarango.meli.challenge.file.domain.File;
import com.oarango.meli.challenge.file.domain.FileReader;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class JsonFileReader implements FileReader {
    private final String FILE_CONTENT_ENCODING;
    private final char FILE_SEPARATOR;

    @Override
    public List<File> readFile(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, FILE_CONTENT_ENCODING));
            String content = bufferedReader.readLine();
            return getContent(content);
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse TXT file : " + e.getMessage());
        }
    }

    private List<File> getContent(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }
}
