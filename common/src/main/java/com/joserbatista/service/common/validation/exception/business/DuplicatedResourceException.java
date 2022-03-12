package com.joserbatista.service.common.validation.exception.business;

import com.joserbatista.service.common.validation.exception.BusinessException;
import lombok.Builder;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
public class DuplicatedResourceException extends BusinessException {

    private static final String ERROR_CODE_DEFAULT = "ERR-409";
    private static final String ERROR_MESSAGE_DEFAULT = "%s <%s> already exists";

    @Builder
    private DuplicatedResourceException(String resourceType, String id, String errorCode, String errorMessage) {
        super(Optional.ofNullable(errorCode).orElse(ERROR_CODE_DEFAULT),
              String.format(Optional.ofNullable(errorMessage).orElse(ERROR_MESSAGE_DEFAULT), resourceType, id));
    }
}
