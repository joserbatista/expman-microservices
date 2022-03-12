package com.joserbatista.service.common.validation.exception.business;

import com.joserbatista.service.common.validation.exception.BusinessException;
import lombok.Builder;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
public class FailedValidationException extends BusinessException {

    private static final String ERROR_CODE_DEFAULT = "ERR-400";
    private static final String ERROR_MESSAGE_DEFAULT = "Validation failed";

    @Builder
    private FailedValidationException(String errorCode, String errorMessage, String errorDescription) {
        super(Optional.ofNullable(errorCode).orElse(ERROR_CODE_DEFAULT),
              Optional.ofNullable(errorMessage).orElse(ERROR_MESSAGE_DEFAULT),
              errorDescription);
    }

}