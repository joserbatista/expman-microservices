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

@Value
@Builder
public class SaveTransactionCommand {

    @Uuid
    String id;

    @Username
    String username;

    @Uuid
    String accountId;

    OffsetDateTime date;

    BigDecimal amount;

    @Valid
    Set<@Word String> tagList;
    String note;
}
