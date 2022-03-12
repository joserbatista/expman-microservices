package com.joserbatista.service.transaction.core.api.command;

import com.joserbatista.service.common.validation.constraint.Username;
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
public class CreateTransactionCommand {

    @Username
    String username;

    @Uuid
    String accountId;

    @NotNull
    OffsetDateTime date;

    @NotNull
    BigDecimal amount;

    @Valid
    Set<@Word String> tagList;
    String note;
}
