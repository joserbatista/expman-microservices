package com.joserbatista.service.common.validation.spring;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@ToString
class ErrorTemplate {

    @NotNull
    @Schema(example = "ERR-001")
    private final String errorCode;

    @NotNull
    @Schema(example = "Parameter validation failure")
    private final String errorMessage;
    @Schema(example = "Parameter is not valid")
    private final String errorDescription;
}
