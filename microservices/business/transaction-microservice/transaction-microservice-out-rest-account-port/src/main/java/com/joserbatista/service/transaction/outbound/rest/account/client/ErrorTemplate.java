package com.joserbatista.service.transaction.outbound.rest.account.client;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
class ErrorTemplate {

    String errorCode;
    String errorMessage;
    String errorDescription;
}
