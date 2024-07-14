package com.oarango.meli.challenge.file.application;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class CsvFileCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String fileType = context.getEnvironment().getProperty("file.content-type");
        return fileType != null && fileType.equals("text/csv");
    }
}
