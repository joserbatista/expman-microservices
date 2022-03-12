package com.joserbatista.service.account.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AccountType")
public enum AccountTypeEnumDto {

    CASH,
    BANK,
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    OTHER
}
