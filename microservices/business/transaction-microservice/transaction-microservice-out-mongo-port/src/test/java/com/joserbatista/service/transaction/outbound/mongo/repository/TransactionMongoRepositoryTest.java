package com.joserbatista.service.transaction.outbound.mongo.repository;

import com.joserbatista.service.transaction.outbound.mongo.MongoConfig;
import com.joserbatista.service.transaction.outbound.mongo.document.TransactionDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataMongoTest
@ActiveProfiles("test")
@ContextConfiguration(classes = MongoConfig.class)
class TransactionMongoRepositoryTest {

    @Autowired
    private TransactionMongoRepository repository;

    private String createdTransactionId;

    @BeforeEach
    void setUp() {
        TransactionDocument transactionDocument = TransactionDocument.builder().id("3c2cd5dc-fd1c-45b5-aee0-b4674cacfb3c")
                                                                     .amount(BigDecimal.valueOf(2.92))
                                                                     .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                     .accountId("22983814-cfd6-41d4-ba00-58c99d4cf956").username("monte8")
                                                                     .note("Locked footage hood duties antarctica bald justin, note.")
                                                                     .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        this.createdTransactionId = this.repository.save(transactionDocument).getId();
    }

    @AfterEach
    void tearDown() {
        this.repository.deleteAll();
    }

    @Test
    void findByAccountUserUsernameAndId_ExistingAccountAndTransaction_IsPresent() {
        Optional<?> transactionDocumentOptional = this.repository.findByUsernameAndId("monte8", this.createdTransactionId);

        Assertions.assertThat(transactionDocumentOptional).isPresent();
    }

    @Test
    void findByUserUsernameAndId_MissingEntities_IsEmpty() {
        org.junit.jupiter.api.Assertions.assertAll(
            () -> Assertions.assertThat(this.repository.findByUsernameAndId("laquit", this.createdTransactionId)).isEmpty(),
            () -> Assertions.assertThat(this.repository.findByUsernameAndId("monte8", "d6a19278-cdcf-4081-a2ea-325f6769becc")).isEmpty(),
            () -> Assertions.assertThat(this.repository.findByUsernameAndId("laquit", "d6a19278-cdcf-4081-a2ea-325f6769becc")).isEmpty()
        );
    }

    @Test
    void findAllByAccountUserUsername_ExistingUser_IsNotEmpty() {
        List<TransactionDocument> transactionDocumentList = this.repository.findAllByUsername("monte8");

        Assertions.assertThat(transactionDocumentList).hasSize(1);
    }

    @Test
    void findAllByAccountUserUsername_MissingUser_IsEmpty() {
        List<TransactionDocument> transactionDocumentList = this.repository.findAllByUsername("laquit");

        Assertions.assertThat(transactionDocumentList).isEmpty();
    }
}