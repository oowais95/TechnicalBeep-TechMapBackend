package com.technicalbeep.techeventsmap.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.debug("Resource not found: {} — {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(base(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
    }

    /**
     * Request-body {@code @Valid} failures ({@link MethodArgumentNotValidException}) and other
     * {@link BindException}s (query/form binding), all mapped to HTTP 400.
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleValidationExceptions(Exception ex, HttpServletRequest request) {
        BindingResult bindingResult = ex instanceof MethodArgumentNotValidException manv
                ? manv.getBindingResult()
                : ((BindException) ex).getBindingResult();
        return validationFailed(bindingResult, request);
    }

    private ResponseEntity<ApiError> validationFailed(BindingResult bindingResult, HttpServletRequest request) {
        List<ApiError.FieldViolation> violations = bindingResult.getFieldErrors().stream()
                .map(this::toViolation)
                .toList();
        log.warn("Validation failed on {}: {}", request.getRequestURI(), summarizeFieldErrors(bindingResult.getFieldErrors()));
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getRequestURI())
                .fieldErrors(violations)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("Constraint violation on {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(base(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "Malformed JSON request";
        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof InvalidFormatException ife) {
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                message = "Invalid value for enum field '%s'".formatted(fieldNameFromInvalidFormat(ife));
            } else {
                message = "Invalid value or format in JSON body";
            }
        } else if (cause instanceof JsonMappingException jme) {
            String detail = jme.getOriginalMessage() != null ? jme.getOriginalMessage() : jme.getMessage();
            message = detail != null ? "JSON mapping error: " + detail : "JSON mapping error";
        }
        log.warn("Unreadable message on {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(base(HttpStatus.BAD_REQUEST, message, request.getRequestURI()));
    }

    private static String fieldNameFromInvalidFormat(InvalidFormatException ife) {
        StringBuilder path = new StringBuilder();
        for (JsonMappingException.Reference ref : ife.getPath()) {
            if (ref.getFieldName() != null) {
                if (!path.isEmpty()) {
                    path.append('.');
                }
                path.append(ref.getFieldName());
            }
        }
        return path.isEmpty() ? "unknown" : path.toString();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String msg = "Invalid value for parameter '%s'".formatted(ex.getName());
        log.warn("Type mismatch on {} parameter {}: {}", request.getRequestURI(), ex.getName(), ex.getValue());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(base(HttpStatus.BAD_REQUEST, msg, request.getRequestURI()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String msg = "Required parameter '%s' is missing".formatted(ex.getParameterName());
        log.warn("Missing parameter on {}: {}", request.getRequestURI(), ex.getParameterName());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(base(HttpStatus.BAD_REQUEST, msg, request.getRequestURI()));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiError> handlePropertyReference(PropertyReferenceException ex, HttpServletRequest request) {
        log.warn("Invalid property in sort/filter on {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(base(HttpStatus.BAD_REQUEST, "Invalid sort or property: " + ex.getPropertyName(), request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error on {}", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(base(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request.getRequestURI()));
    }

    private ApiError.FieldViolation toViolation(FieldError fe) {
        return new ApiError.FieldViolation(fe.getField(), fe.getDefaultMessage());
    }

    private static String summarizeFieldErrors(List<FieldError> errors) {
        return errors.stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
    }

    private ApiError base(HttpStatus status, String message, String path) {
        return ApiError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }
}
