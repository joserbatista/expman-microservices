package com.joserbatista.service.user.outbound.mongo.document;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("users")
@Data
@Builder
public class UserDocument {

    @Id
    private String documentId;

    @Builder.Default
    @Indexed(unique = true)
    private String id = UUID.randomUUID().toString();

    @Indexed(unique = true)
    private String username;
    @ToString.Exclude
    private String password;

    @Indexed(unique = true)
    private String email;

    private String fullName;

    private Boolean enabled;
}
