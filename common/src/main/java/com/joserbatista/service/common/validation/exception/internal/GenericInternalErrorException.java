package com.joserbatista.service.common.validation.exception.internal;

import com.joserbatista.service.common.validation.exception.InternalErrorException;
import lombok.Builder;

public class GenericInternalErrorException extends InternalErrorException {

    @Builder
    protected GenericInternalErrorException(String errorDescription, String intention,
        Throwable cause) {
        super("ERR-005", "An internal error has occurred", errorDescription, intention);
        this.initCause(cause);
    }
}