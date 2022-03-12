package com.joserbatista.service.common.validation.spring;

import com.joserbatista.service.common.validation.exception.business.AccessDeniedException;
import com.joserbatista.service.common.validation.exception.business.DuplicatedResourceException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        this.exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handle_ResourceNotFoundException_ReturnsValidErrorTemplate() {
        ResourceNotFoundException exception = ResourceNotFoundException.builder()
                                                                       .resourceType("Account")
                                                                       .id("6bc08985-2b20-4c78-9b76-3e4d05ec1bc0")
                                                                       .build();
        ResponseEntity<ErrorTemplate> response = this.exceptionHandler.handle(exception);

        ErrorTemplate body = response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getErrorCode()).isEqualTo("ERR-404");
        Assertions.assertThat(body.getErrorMessage()).isEqualTo("Account <6bc08985-2b20-4c78-9b76-3e4d05ec1bc0> not found");
    }

    @Test
    void handle_FailedValidationException_ReturnsValidErrorTemplate() {
        ResponseEntity<ErrorTemplate> response = this.exceptionHandler.handle(FailedValidationException.builder().build());

        ErrorTemplate body = response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getErrorCode()).isEqualTo("ERR-400");
        Assertions.assertThat(body.getErrorMessage()).isEqualTo("Validation failed");
    }

    @Test
    void handle_DuplicatedResourceException_ReturnsValidErrorTemplate() {
        ResponseEntity<ErrorTemplate> response = this.exceptionHandler.handle(
            DuplicatedResourceException.builder().resourceType("Account").id("6bc08985-2b20-4c78-9b76-3e4d05ec1bc0").build());

        ErrorTemplate body = response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getErrorCode()).isEqualTo("ERR-409");
        Assertions.assertThat(body.getErrorMessage()).isEqualTo("Account <6bc08985-2b20-4c78-9b76-3e4d05ec1bc0> already exists");
    }

    @Test
    void handle_AccessDeniedException_ReturnsValidErrorTemplate() {
        ResponseEntity<ErrorTemplate> response = this.exceptionHandler.handle(AccessDeniedException.builder().build());

        ErrorTemplate body = response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getErrorCode()).isEqualTo("ERR-403");
        Assertions.assertThat(body.getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    void handle_DateTimeParseException_ReturnsValidErrorTemplate() {
        ResponseEntity<Object> response = this.exceptionHandler.handle(
            new DateTimeParseException("Text '2014-11-01' could not be parsed at index 0", "2014-11-01", 0)
        );

        ErrorTemplate body = (ErrorTemplate) response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getErrorCode()).isEqualTo("ERR-400");
        Assertions.assertThat(body.getErrorMessage()).isEqualTo("Validation failed");
        Assertions.assertThat(body.getErrorDescription()).isEqualTo("Text '2014-11-01' could not be parsed at index 0");
    }

    @Test
    void handleBindException_BindException_ReturnsValidErrorTemplate() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class, Mockito.RETURNS_DEEP_STUBS);
        FieldError fieldError = Mockito.mock(FieldError.class);
        Mockito.when(fieldError.getField()).thenReturn("getUserTransactionById.id");
        Mockito.when(fieldError.getRejectedValue()).thenReturn("shalona_burriss1urg@audi.wu");
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Object> response = this.exceptionHandler.handleBindException(new BindException(bindingResult), null, null, null);

        ErrorTemplate body = (ErrorTemplate) response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getErrorCode()).isEqualTo("ERR-400");
        Assertions.assertThat(body.getErrorMessage()).isEqualTo("Validation failed");
        Assertions.assertThat(body.getErrorDescription())
                  .isEqualTo("Value <shalona_burriss1urg@audi.wu> rejected for field <getUserTransactionById.id>");
    }

    @Test
    void handle_ConstraintViolationException_ReturnsValidErrorTemplate() {
        ConstraintViolationException exception = Mockito.mock(ConstraintViolationException.class);
        ConstraintViolation<?> constraintViolation = Mockito.mock(ConstraintViolation.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(constraintViolation.getInvalidValue()).thenReturn("shalona_burriss1urg@audi.wu");
        Mockito.when(constraintViolation.getPropertyPath().toString()).thenReturn("getUserTransactionById.id");

        Mockito.when(exception.getConstraintViolations()).thenReturn(Set.of(constraintViolation));

        String expectedErrorDescription = "Value <shalona_burriss1urg@audi.wu> rejected for field <getUserTransactionById.id>";
        ResponseEntity<Object> response = this.exceptionHandler.handle(exception);

        ErrorTemplate body = (ErrorTemplate) response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getErrorCode()).isEqualTo("ERR-400");
        Assertions.assertThat(body.getErrorMessage()).isEqualTo("Validation failed");
        Assertions.assertThat(body.getErrorDescription()).isEqualTo(expectedErrorDescription);
    }
}