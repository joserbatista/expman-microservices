package com.joserbatista.service.transaction.outbound.mongo.mapper;

import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.outbound.mongo.document.TransactionDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = TransactionMongoMapperImpl.class)
class TransactionMongoMapperTest {

    @Autowired
    private TransactionMongoMapper mapper;

    @Test
    void toAccountTransactionList_FromTransactionDocumentList_IsProperlyMapped() {
        TransactionDocument transactionDocument = TransactionDocument.builder().id("3c2cd5dc-fd1c-45b5-aee0-b4674cacfb3c")
                                                                     .amount(BigDecimal.valueOf(2.92))
                                                                     .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                     .accountId("ad5f5f2e-bab6-4f96-afea-b17090d30bde").username("laquetteba")
                                                                     .note("Locked footage hood duties antarctica bald justin, note.")
                                                                     .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        List<TransactionDocument> transactionDocumentList = List.of(transactionDocument);
        List<Transaction> actual = this.mapper.toTransactionList(transactionDocumentList);
        List<Transaction> expected = List.of(Transaction.builder().id("3c2cd5dc-fd1c-45b5-aee0-b4674cacfb3c")
                                                        .amount(BigDecimal.valueOf(2.92)).date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                        .note("Locked footage hood duties antarctica bald justin, note.")
                                                        .accountId("ad5f5f2e-bab6-4f96-afea-b17090d30bde")
                                                        .tagList(Set.of("Syria", "SolarRoad", "Annada"))
                                                        .build());

        Assertions.assertThat(this.mapper.toTransaction(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toTransactionDocumentList_FromTransactionList_IsProperlyMapped() {
        CreateTransactionCommand command = CreateTransactionCommand.builder()
                                                                   .amount(BigDecimal.valueOf(2.92))
                                                                   .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                   .note("Locked footage hood duties antarctica bald justin, note.")
                                                                   .accountId("03d3859f-5225-447b-bb32-51ac33eb69db").username("laquetteba")
                                                                   .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        List<TransactionDocument> actual = this.mapper.toTransactionDocumentList(List.of(command));

        List<TransactionDocument> expected = List.of(
            TransactionDocument.builder()
                               .amount(BigDecimal.valueOf(2.92)).date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                               .note("Locked footage hood duties antarctica bald justin, note.")
                               .accountId("03d3859f-5225-447b-bb32-51ac33eb69db").username("laquetteba")
                               .tagList(Set.of("Syria", "SolarRoad", "Annada")).build()
        );

        Assertions.assertThat(this.mapper.toTransactionDocumentList(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    void toTransactionDocument_FromSaveTransactionCommand_IsProperlyMapped() {
        SaveTransactionCommand command = SaveTransactionCommand.builder().username("laquetteba").id("3c2cd5dc-fd1c-45b5-aee0-b4674cacfb3c")
                                                               .amount(BigDecimal.valueOf(2.92)).date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                               .note("Locked footage hood duties antarctica bald justin, note.")
                                                               .accountId("03d3859f-5225-447b-bb32-51ac33eb69db")
                                                               .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        TransactionDocument actual = this.mapper.toTransactionDocument(command, "cl013x5kz0002ec13cmb3cxnu");
        TransactionDocument expected = TransactionDocument.builder().documentId("cl013x5kz0002ec13cmb3cxnu").id(
                                                              "3c2cd5dc-fd1c-45b5-aee0-b4674cacfb3c")
                                                          .amount(BigDecimal.valueOf(2.92)).date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                          .note("Locked footage hood duties antarctica bald justin, note.")
                                                          .accountId("03d3859f-5225-447b-bb32-51ac33eb69db").username("laquetteba")
                                                          .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        Assertions.assertThat(this.mapper.toTransactionDocument(null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

}