package com.joserbatista.service.user.core.api.command;

import com.joserbatista.service.common.validation.constraint.Username;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Email;

@Value
@Builder
public class SaveUserCommand {

    @Username
    String username;
    @ToString.Exclude
    String password;

    String fullName;
    @Email
    String email;
}