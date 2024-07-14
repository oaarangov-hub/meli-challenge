package com.oarango.meli.challenge.file.infrastructure.repository;

import com.oarango.meli.challenge.file.domain.File;
import com.oarango.meli.challenge.file.domain.FileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TxtFileReader implements FileReader {
    private final String FILE_CONTENT_ENCODING;
    private final char FILE_SEPARATOR;

    @Override
    public List<File> readFile(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, FILE_CONTENT_ENCODING));
            List<String> content = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.add(line);
            }
            return getContent(content);
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse TXT file : " + e.getMessage());
        }
    }

    private List<File> getContent(List<String> lines) {
        List<File> records = new ArrayList<>();
        lines.forEach(record -> {
            String[] fields = record.split(String.valueOf(FILE_SEPARATOR));
            String site = fields[0];
            String id = fields[1];
            try {
                records.add(File.builder()
                        .site(site)
                        .id(Long.parseLong(id))
                        .build());
            } catch (NumberFormatException e) {
                log.error("Error parsing id: {}", id);
            }
        });
        return records;
    }
}
