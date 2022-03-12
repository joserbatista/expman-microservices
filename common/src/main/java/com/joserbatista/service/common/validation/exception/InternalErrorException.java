package com.joserbatista.service.common.validation.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * Represents an exception related with internal errors.
 */

@Getter
@ToString(callSuper = true)
public abstract class InternalErrorException extends BaseException {

    private final String intention;

    protected InternalErrorException(String errorCode, String errorMessage, String errorDescription, String intention) {
        super(errorCode, errorMessage, errorDescription);
        this.intention = intention;
    }

}
