package com.joserbatista.service.importer.core.domain;

import com.joserbatista.service.common.validation.constraint.Uuid;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Account {

    @Uuid
    String id;
    String name;

    String notes;
    Boolean active;

    BigDecimal amount;

    AccountType type;

}
