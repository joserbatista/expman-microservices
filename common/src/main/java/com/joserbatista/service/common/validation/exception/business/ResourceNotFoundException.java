package com.joserbatista.service.common.validation.exception.business;

import com.joserbatista.service.common.validation.exception.BusinessException;
import lombok.Builder;
import lombok.ToString;

import java.util.Optional;
import javax.validation.constraints.NotEmpty;

@ToString(callSuper = true)
public class ResourceNotFoundException extends BusinessException {

    private static final String ERROR_CODE_DEFAULT = "ERR-404";
    private static final String ERROR_MESSAGE_DEFAULT = "%s <%s> not found";

    @Builder
    private ResourceNotFoundException(@NotEmpty String resourceType, @NotEmpty String id, String errorCode, String errorMessage) {
        super(Optional.ofNullable(errorCode).orElse(ERROR_CODE_DEFAULT),
              String.format(Optional.ofNullable(errorMessage).orElse(ERROR_MESSAGE_DEFAULT),
                            Optional.ofNullable(resourceType).orElse("Resource"), id));
    }
}
