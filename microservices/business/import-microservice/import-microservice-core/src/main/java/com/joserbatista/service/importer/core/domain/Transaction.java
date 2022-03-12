package com.joserbatista.service.importer.core.domain;

import com.joserbatista.service.common.validation.constraint.Uuid;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class Transaction {

    String id;

    @Uuid
    String accountId;

    @NotNull
    BigDecimal amount;

    List<String> tagList;

    OffsetDateTime date;
    String note;
}
