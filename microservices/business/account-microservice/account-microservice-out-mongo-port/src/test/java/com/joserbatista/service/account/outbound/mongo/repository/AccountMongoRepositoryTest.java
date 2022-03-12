package com.joserbatista.service.account.outbound.mongo.repository;

import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.account.outbound.mongo.MongoConfig;
import com.joserbatista.service.account.outbound.mongo.document.AccountDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

@DataMongoTest
@ActiveProfiles("test")
@ContextConfiguration(classes = MongoConfig.class)
class AccountMongoRepositoryTest {

    @Autowired
    private AccountMongoRepository repository;

    private String createdAccountId;

    @BeforeEach
    void setUp() {
        AccountDocument accountDocument = AccountDocument.builder()
                                                         .name("Credit Account").type(AccountType.BANK).active(true)
                                                         .notes("Talk ending coalition flashers databases serve.").username("monte8")
                                                         .build();

        this.createdAccountId = this.repository.save(accountDocument).getId();
    }

    @AfterEach
    void tearDown() {
        this.repository.deleteAll();
    }

    @Test
    void findByUserUsernameAndId_ExistingUserAndAccount_IsPresent() {
        Optional<AccountDocument> accountDocumentOptional = this.repository.findByUsernameAndId("monte8", this.createdAccountId);

        Assertions.assertThat(accountDocumentOptional).isPresent();
    }

    @Test
    void findByUserUsernameAndId_MissingEntities_IsEmpty() {
        org.junit.jupiter.api.Assertions.assertAll(
            () -> Assertions.assertThat(this.repository.findByUsernameAndId("laquithad", this.createdAccountId)).isEmpty(),
            () -> Assertions.assertThat(this.repository.findByUsernameAndId("monte8", "d6a19278-cdcf-4081-a2ea-325f6769becc")).isEmpty(),
            () -> Assertions.assertThat(this.repository.findByUsernameAndId("laquithad", "d6a19278-cdcf-4081-a2ea-325f6769becc")).isEmpty()
        );
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