package com.joserbatista.service.importer.core.api.command;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.importer.core.domain.AccountType;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class CreateAccountCommand {

    @Username
    String username;

    @NotBlank
    String name;
    String notes;
    @Builder.Default
    AccountType type = AccountType.OTHER;
}
