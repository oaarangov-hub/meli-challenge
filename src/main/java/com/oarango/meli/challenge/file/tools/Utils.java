package com.oarango.meli.challenge.file.tools;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class Utils {
    public static boolean hasValidContentType(MultipartFile file, String contentType) {
        return Objects.equals(file.getContentType(), contentType);
    }
}
