package com.joserbatista.service.importer.core.api.command;

import com.joserbatista.service.common.validation.constraint.Username;
import lombok.Builder;
import lombok.Value;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class ImportTransactionCommand {

    @Username
    String username;
    @NotNull
    Resource resource;
    String contentType;
}
