package com.joserbatista.service.common.validation.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * This exception is the base class for all exceptions thrown by the services.
 */

@Getter
@ToString(callSuper = true)
public abstract class BaseException extends Exception {

    private static final long serialVersionUID = -3297642201979689330L;

    private final String errorCode;
    private final String errorMessage;
    private final String errorDescription;

    BaseException(String errorCode, String errorMessage, String errorDescription) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDescription = errorDescription;
    }

    BaseException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDescription = null;
    }
}
