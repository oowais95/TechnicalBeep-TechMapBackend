package com.technicalbeep.techeventsmap.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Accepts {@code null} or blank; otherwise the value must be a syntactically valid URL (see {@link OptionalUrlValidator}).
 */
@Documented
@Constraint(validatedBy = OptionalUrlValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface OptionalUrl {

    String message() default "must be a valid URL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
