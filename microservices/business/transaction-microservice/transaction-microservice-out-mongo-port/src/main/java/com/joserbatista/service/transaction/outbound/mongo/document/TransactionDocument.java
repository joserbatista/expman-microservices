package com.joserbatista.service.transaction.outbound.mongo.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Document("transactions")
@Data
@Builder
public class TransactionDocument {

    @Id
    private String documentId;

    @Builder.Default
    @Indexed(unique = true)
    private String id = UUID.randomUUID().toString();

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;

    private Set<String> tagList;

    private OffsetDateTime date;
    private String note;

    private String accountId;
    private String username;
}
