package com.joserbatista.service.importer.core.api.command;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Word;
import com.joserbatista.service.importer.core.domain.Account;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class CreateTransactionCommand {

    @Username
    String username;

    @NotNull
    @Valid
    Account account;

    @NotNull
    OffsetDateTime date;

    @NotNull
    BigDecimal amount;

    @Valid
    List<@Word String> tagList;
    String note;
}
