package com.joserbatista.service.importer.outbound.mongo.document;

import com.joserbatista.service.importer.core.domain.AccountType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Document("accounts")
@Data
@Builder
public class AccountDocument {

    @Id
    private String documentId;

    @Builder.Default
    @Indexed(unique = true)
    private String id = UUID.randomUUID().toString();

    private String name;

    private String notes;

    @Builder.Default
    private boolean active = true;

    private BigDecimal amount;

    private AccountType type;

    private String username;
}
