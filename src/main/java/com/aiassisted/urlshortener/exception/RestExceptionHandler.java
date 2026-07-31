package com.aiassisted.urlshortener.exception;

import com.aiassisted.urlshortener.config.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "VALIDATION_ERROR");
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        body.put("message", "Validation failed");
        body.put("path", request.getRequestURI());
        body.put("requestId", resolveRequestId(request));
        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                              HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "INVALID_REQUEST");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("requestId", resolveRequestId(request));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                               HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "NOT_FOUND");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("requestId", resolveRequestId(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex,
                                                                       HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "CONFLICT");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("requestId", resolveRequestId(request));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    private String resolveRequestId(HttpServletRequest request) {
        String requestId = MDC.get(RequestIdFilter.MDC_KEY);
        if (requestId != null && !requestId.isBlank()) {
            return requestId;
        }
        return request.getHeader(RequestIdFilter.REQUEST_ID_HEADER);
    }
}
