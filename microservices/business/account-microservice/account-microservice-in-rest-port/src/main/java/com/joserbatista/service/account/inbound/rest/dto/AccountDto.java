package com.joserbatista.service.account.inbound.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@Schema(name = "Account")
public class AccountDto {

    String id;

    String name;
    String notes;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Boolean active;
    AccountTypeEnumDto type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    BigDecimal amount;

}
