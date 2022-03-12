package com.joserbatista.service.common.validation.spring;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.BusinessException;
import com.joserbatista.service.common.validation.exception.InternalErrorException;
import com.joserbatista.service.common.validation.exception.OutboundInvocationException;
import com.joserbatista.service.common.validation.exception.business.AccessDeniedException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MESSAGE_HANDLING_WITH_STATUS = "Handling {} with status {}";
    private static final String MESSAGE_CAUGHT = "{} caught: {}";
    private static final Map<Class<? extends BaseException>, HttpStatus> BASE_EXCEPTION_HTTP_STATUS_MAP =
        Map.of(
            BusinessException.class, HttpStatus.BAD_REQUEST,
            InternalErrorException.class, HttpStatus.INTERNAL_SERVER_ERROR,
            OutboundInvocationException.class, HttpStatus.BAD_GATEWAY,
            AccessDeniedException.class, HttpStatus.FORBIDDEN,
            ResourceNotFoundException.class, HttpStatus.NOT_FOUND
        );

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorTemplate> handle(BaseException ex) {
        HttpStatus status = BASE_EXCEPTION_HTTP_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.BAD_REQUEST);
        log.debug(MESSAGE_HANDLING_WITH_STATUS, ex, status);

        ErrorTemplate body = ErrorTemplate.builder()
                                          .errorCode(ex.getErrorCode()).errorMessage(ex.getErrorMessage()).errorDescription(ex.getErrorDescription())
                                          .build();

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handle(DateTimeParseException ex) {
        log.debug(MESSAGE_CAUGHT, ex.getClass(), ex.getMessage());
        return this.wrapToConstraintViolatedException(ex.getMessage(), ex.getClass().getName());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handle(ConstraintViolationException ex) {
        log.debug(MESSAGE_CAUGHT, ex.getClass(), ex.getMessage());
        String message = Stream.ofNullable(ex.getConstraintViolations()).flatMap(Collection::stream)
                               .map(error -> String.format("Value <%s> rejected for field <%s>", error.getInvalidValue(), error.getPropertyPath()))
                               .collect(Collectors.joining(", "));

        return this.wrapToConstraintViolatedException(message, ex.getClass().getName());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        return handleBindException(ex, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.debug(MESSAGE_CAUGHT, ex.getClass(), ex.getMessage());

        String message = Optional.ofNullable(ex.getCause()).filter(InvalidFormatException.class::isInstance).map(InvalidFormatException.class::cast)
                                 .map(error -> String.format("Value <%s> rejected", error.getValue())).orElse("A value was rejected");

        return this.wrapToConstraintViolatedException(message, ex.getClass().getName());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug(MESSAGE_CAUGHT, ex.getClass(), ex.getMessage());

        String message = Stream.ofNullable(ex.getBindingResult()).map(Errors::getFieldErrors).flatMap(Collection::stream)
                               .map(error -> String.format("Value <%s> rejected for field <%s>", error.getRejectedValue(), error.getField()))
                               .collect(Collectors.joining(", "));

        return this.wrapToConstraintViolatedException(message, ex.getClass().getName());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, final HttpHeaders headers,
        final HttpStatus status, final WebRequest request) {
        log.debug(MESSAGE_CAUGHT, ex.getClass(), ex.getMessage());

        String message = String.format("Value rejected for field <%s>", ex.getRequestPartName());

        return this.wrapToConstraintViolatedException(message, ex.getClass().getName());
    }

    private ResponseEntity<Object> wrapToConstraintViolatedException(String message, String originalExceptionName) {
        log.debug(MESSAGE_HANDLING_WITH_STATUS, originalExceptionName, HttpStatus.BAD_REQUEST);

        FailedValidationException constraintViolatedException = FailedValidationException.builder().errorDescription(message).build();

        log.debug("{} wrapped to {}", originalExceptionName, constraintViolatedException.getClass().getName());

        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                                                   .errorCode(constraintViolatedException.getErrorCode())
                                                   .errorMessage(constraintViolatedException.getErrorMessage())
                                                   .errorDescription(constraintViolatedException.getErrorDescription()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorTemplate);
    }
}
