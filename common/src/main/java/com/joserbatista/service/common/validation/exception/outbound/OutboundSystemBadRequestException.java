package com.joserbatista.service.common.validation.exception.outbound;

import com.joserbatista.service.common.validation.exception.OutboundInvocationException;
import lombok.Builder;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
public class OutboundSystemBadRequestException extends OutboundInvocationException {

    private static final String ERROR_CODE_DEFAULT = "ERR-004";
    private static final String MESSAGE_DEFAULT = "Invalid request to external system";

    @Builder
    private OutboundSystemBadRequestException(String errorCode, String errorMessage, String system, String errorDescription,
        String systemStatusCode) {
        super(Optional.ofNullable(errorCode).orElse(ERROR_CODE_DEFAULT),
              Optional.ofNullable(errorMessage).orElse(MESSAGE_DEFAULT),
              errorDescription, system, systemStatusCode, null);
    }
}
