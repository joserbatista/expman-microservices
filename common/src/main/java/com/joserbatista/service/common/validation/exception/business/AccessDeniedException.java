package com.joserbatista.service.common.validation.exception.business;

import com.joserbatista.service.common.validation.exception.BusinessException;
import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public class AccessDeniedException extends BusinessException {

    private static final String ERROR_CODE_DEFAULT = "ERR-403";
    private static final String ERROR_MESSAGE_DEFAULT = "Access Denied";

    @Builder
    private AccessDeniedException(String errorDescription) {
        super(ERROR_CODE_DEFAULT, ERROR_MESSAGE_DEFAULT, errorDescription);
    }
}
