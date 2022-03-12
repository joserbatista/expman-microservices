package com.joserbatista.service.transaction.core.domain;

import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.constraint.Word;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class Transaction {

    String id;

    @Uuid
    String accountId;

    @NotNull
    BigDecimal amount;

    @Valid
    Set<@Word String> tagList;

    OffsetDateTime date;
    String note;
}
