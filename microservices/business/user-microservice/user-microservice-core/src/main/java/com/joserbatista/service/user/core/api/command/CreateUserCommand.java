package com.joserbatista.service.user.core.api.command;

import com.joserbatista.service.common.validation.constraint.Username;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder
public class CreateUserCommand {

    @Username
    String username;
    @NotBlank
    @ToString.Exclude
    String password;
    String fullName;
    @NotBlank
    @Email
    String email;
}