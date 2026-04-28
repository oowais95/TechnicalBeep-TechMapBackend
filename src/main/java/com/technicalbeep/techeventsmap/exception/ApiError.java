package com.technicalbeep.techeventsmap.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldViolation> fieldErrors
) {
    public record FieldViolation(String field, String message) {
    }
}
