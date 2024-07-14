package com.oarango.meli.challenge.file.application;

import com.oarango.meli.challenge.file.domain.FileReader;
import com.oarango.meli.challenge.file.infrastructure.repository.CsvFileReader;
import com.oarango.meli.challenge.file.infrastructure.repository.JsonFileReader;
import com.oarango.meli.challenge.file.infrastructure.repository.TxtFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileReaderConfig {
    @Value("${file.content.encoding}")
    private String FILE_CONTENT_ENCODING;
    @Value("${file.content.separator}")
    private char FILE_SEPARATOR;

    @Bean
    @Conditional(CsvFileCondition.class)
    public FileReader csvFileReader() {
        return new CsvFileReader(FILE_CONTENT_ENCODING, FILE_SEPARATOR);
    }

    @Bean
    @Conditional(TxtFileCondition.class)
    public FileReader txtFileReader() {
        return new TxtFileReader(FILE_CONTENT_ENCODING, FILE_SEPARATOR);
    }

    @Bean
    @Conditional(JsonFileCondition.class)
    public FileReader jsonFileReader() {
        return new JsonFileReader(FILE_CONTENT_ENCODING, FILE_SEPARATOR);
    }
}
