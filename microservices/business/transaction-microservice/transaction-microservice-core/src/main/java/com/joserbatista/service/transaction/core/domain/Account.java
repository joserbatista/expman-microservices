package com.joserbatista.service.transaction.core.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Account {

    String id;
    String name;
}
