package com.joserbatista.service.account.core.api.command;

import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.common.validation.constraint.Username;
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
