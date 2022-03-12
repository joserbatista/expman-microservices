package com.joserbatista.service.importer.outbound.mongo.repository;

import com.joserbatista.service.importer.core.domain.AccountType;
import com.joserbatista.service.importer.outbound.mongo.MongoConfig;
import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.importer.outbound.mongo.document.TransactionDocument;
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
import java.util.Set;

@DataMongoTest
@ActiveProfiles("test")
@ContextConfiguration(classes = MongoConfig.class)
class TransactionMongoRepositoryTest {

    @Autowired
    private AccountMongoRepository accountMongoRepository;

    @Autowired
    private TransactionMongoRepository repository;

    private String createdTransactionId;

    @BeforeEach
    void setUp() {
        AccountDocument accountDocument = AccountDocument.builder()
                                                         .name("Credit Account").type(AccountType.BANK).active(true)
                                                         .notes("Talk ending coalition flashers databases serve.").username("monte8")
                                                         .build();

        AccountDocument targetAccount = this.accountMongoRepository.save(accountDocument);
        TransactionDocument transactionDocument = TransactionDocument.builder().id("3c2cd5dc-fd1c-45b5-aee0-b4674cacfb3c")
                                                                     .amount(BigDecimal.valueOf(2.92))
                                                                     .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                     .accountId(targetAccount.getId()).username("monte8")
                                                                     .note("Locked footage hood duties antarctica bald justin, note.")
                                                                     .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        this.createdTransactionId = this.repository.save(transactionDocument).getDocumentId();
    }

    @AfterEach
    void tearDown() {
        this.accountMongoRepository.deleteAll();
        this.repository.deleteAll();
    }

    @Test
    void existsById_Existing_IsTrue() {
        Assertions.assertThat(this.repository.existsById(this.createdTransactionId)).isTrue();
    }

}