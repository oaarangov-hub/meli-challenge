package com.oarango.meli.challenge.file.domain;

import java.io.InputStream;
import java.util.List;

public interface FileReader {
    List<File> readFile(InputStream inputStream);
}
