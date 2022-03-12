package com.joserbatista.service.transaction.inbound.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joserbatista.service.common.validation.constraint.Uuid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Value
@Builder
@Schema(name = "Transaction")
public class TransactionDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Uuid
    String id;

    @Uuid
    String accountId;
    OffsetDateTime date;
    BigDecimal amount;
    Set<String> tagList;
    String note;
}
