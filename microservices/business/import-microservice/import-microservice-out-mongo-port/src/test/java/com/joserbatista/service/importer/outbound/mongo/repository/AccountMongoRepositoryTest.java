package com.joserbatista.service.importer.outbound.mongo.repository;

import com.joserbatista.service.importer.core.domain.AccountType;
import com.joserbatista.service.importer.outbound.mongo.MongoConfig;
import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
@ContextConfiguration(classes = MongoConfig.class)
class AccountMongoRepositoryTest {

    @Autowired
    private AccountMongoRepository repository;

    @BeforeEach
    void setUp() {
        AccountDocument accountDocument = AccountDocument.builder()
                                                         .name("Credit Account").type(AccountType.BANK).active(true)
                                                         .notes("Talk ending coalition flashers databases serve.").username("monte8")
                                                         .build();

        this.repository.save(accountDocument);
    }

    @AfterEach
    void tearDown() {
        this.repository.deleteAll();
    }

    @Test
    void findByUserUsername_ExistingUser_IsNotEmpty() {
        List<AccountDocument> accountDocumentOptional = this.repository.findAllByUsername("monte8");

        Assertions.assertThat(accountDocumentOptional).hasSize(1);
    }

    @Test
    void findByUserUsername_MissingUser_IsEmpty() {
        List<AccountDocument> accountDocumentOptional = this.repository.findAllByUsername("laquithad");

        Assertions.assertThat(accountDocumentOptional).isEmpty();
    }

    @Test
    void existsByUserUsernameAndName_ExistingUserAndAccount_IsTrue() {
        Assertions.assertThat(this.repository.existsByUsernameAndName("monte8", "Credit Account")).isTrue();
    }

    @Test
    void existsByUserUsernameAndName_MissingEntities_IsFalse() {
        org.junit.jupiter.api.Assertions.assertAll(
            () -> Assertions.assertThat(this.repository.existsByUsernameAndName("laquithad", "Credit Account")).isFalse(),
            () -> Assertions.assertThat(this.repository.existsByUsernameAndName("monte8", "Debit Account")).isFalse(),
            () -> Assertions.assertThat(this.repository.existsByUsernameAndName("laquithad", "Debit Account")).isFalse()
        );
    }
}