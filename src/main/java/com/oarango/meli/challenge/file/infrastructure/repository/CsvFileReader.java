package com.oarango.meli.challenge.file.infrastructure.repository;

import com.oarango.meli.challenge.file.domain.File;
import com.oarango.meli.challenge.file.domain.FileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CsvFileReader implements FileReader {
    private final String FILE_CONTENT_ENCODING;
    private final char FILE_SEPARATOR;

    @Override
    public List<File> readFile(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, FILE_CONTENT_ENCODING));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.newFormat(FILE_SEPARATOR).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            return getContent(csvRecords);
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file : " + e.getMessage());
        }
    }

    private List<File> getContent(Iterable<CSVRecord> csvRecords) {
        List<File> lines = new ArrayList<>();
        csvRecords.forEach(record -> {
            String site = record.get("site");
            String id = record.get("id");
            try {
                lines.add(File.builder()
                        .site(site)
                        .id(Long.parseLong(id))
                        .build());
            } catch (NumberFormatException e) {
                log.error("Error parsing id: {}", id);
            }
        });
        return lines;
    }
}
