package com.joserbatista.service.common.validation.exception.outbound;

import com.joserbatista.service.common.validation.exception.OutboundInvocationException;
import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public class OutboundSystemUnexpectedErrorException extends OutboundInvocationException {

    @Builder
    private OutboundSystemUnexpectedErrorException(String errorDescription, String system, String systemStatusCode, Throwable systemCause) {
        super("ERR-007", "Outbound call produced an unexpected error", errorDescription, system, systemStatusCode, systemCause);
    }
}
