package com.technicalbeep.techeventsmap.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URL;

public class OptionalUrlValidator implements ConstraintValidator<OptionalUrl, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String trimmed = value.strip();
        if (trimmed.length() > 2048) {
            return false;
        }
        try {
            new URL(trimmed);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
