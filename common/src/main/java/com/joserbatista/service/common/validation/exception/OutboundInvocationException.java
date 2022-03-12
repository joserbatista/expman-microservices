package com.joserbatista.service.common.validation.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * Represents an exception that occurred while invoking an external system.
 */

@Getter
@ToString(callSuper = true)
public abstract class OutboundInvocationException extends BaseException {

    private final String system;
    private final String systemStatusCode;

    protected OutboundInvocationException(String errorCode, String errorMessage, String errorDescription, String system,
        String systemStatusCode,
        Throwable systemCause) {
        super(errorCode, errorMessage, errorDescription);
        this.initCause(systemCause);
        this.system = system;
        this.systemStatusCode = systemStatusCode;
    }
}
