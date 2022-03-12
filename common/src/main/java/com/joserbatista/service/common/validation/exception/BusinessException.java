package com.joserbatista.service.common.validation.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * Represents an exception related with business logic.
 */

@Getter
@ToString(callSuper = true)
public abstract class BusinessException extends BaseException {

    protected BusinessException(String errorCode, String errorMessage, String errorDescription) {
        super(errorCode, errorMessage, errorDescription);
    }

    protected BusinessException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
