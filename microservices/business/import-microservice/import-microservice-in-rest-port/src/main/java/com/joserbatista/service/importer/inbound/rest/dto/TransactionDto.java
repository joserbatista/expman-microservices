package com.joserbatista.service.importer.inbound.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joserbatista.service.common.validation.constraint.Uuid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
@Schema(name = "Transaction")
public class TransactionDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String id;

    @Uuid
    String accountId;
    OffsetDateTime date;
    BigDecimal amount;
    List<String> tagList;
    String note;
}
