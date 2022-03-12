package com.joserbatista.service.user.inbound.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joserbatista.service.common.validation.constraint.Username;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder
@Schema(name = "User")
public class UserDto {

    @Username
    String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @ToString.Exclude
    String password;
    @Email
    @NotBlank
    String email;
    String fullName;

}
