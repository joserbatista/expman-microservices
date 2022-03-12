package com.joserbatista.service.importer.outbound.mongo.mapper;

import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.domain.AccountType;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.outbound.mongo.document.TransactionDocument;
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
                                                        .tagList(List.of("Syria", "SolarRoad", "Annada"))
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
                                                                   .account(this.buildAccountList().get(0)).username("laquetteba")
                                                                   .tagList(List.of("Syria", "SolarRoad", "Annada")).build();

        List<TransactionDocument> actual = this.mapper.toTransactionDocumentList(List.of(command));

        List<TransactionDocument> expected = List.of(
            TransactionDocument.builder()
                               .amount(BigDecimal.valueOf(2.92)).date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                               .note("Locked footage hood duties antarctica bald justin, note.")
                               .accountId("ad5f5f2e-bab6-4f96-afea-b17090d30bde").username("laquetteba")
                               .tagList(Set.of("Syria", "SolarRoad", "Annada")).build()
        );

        Assertions.assertThat(this.mapper.toTransactionDocumentList(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().ignoringFields("id").isEqualTo(expected);
    }

    //<editor-fold desc="builders">
    private List<Account> buildAccountList() {
        return List.of(
            Account.builder()
                   .id("ad5f5f2e-bab6-4f96-afea-b17090d30bde").name("BCP").type(AccountType.BANK)
                   .active(true).amount(BigDecimal.valueOf(256L)).notes("Millenium BCP")
                   .build(),
            Account.builder()
                   .id("3cf60827-bf1c-417d-abc8-781beb4425bd").name("PayPal").type(AccountType.PAYPAL)
                   .active(true).amount(BigDecimal.ZERO).notes("Elon Musk helped me open this account")
                   .build()
        );
    }
    //</editor-fold>

}