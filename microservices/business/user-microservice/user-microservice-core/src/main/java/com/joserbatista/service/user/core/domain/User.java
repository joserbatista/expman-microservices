package com.joserbatista.service.user.core.domain;

import com.joserbatista.service.common.validation.constraint.Username;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class User {

    @Username
    String username;
    @NotBlank
    @ToString.Exclude
    String password;
    String fullName;
    String email;
}
