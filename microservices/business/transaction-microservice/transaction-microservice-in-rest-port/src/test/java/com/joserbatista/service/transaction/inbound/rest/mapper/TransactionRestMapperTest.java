package com.joserbatista.service.transaction.inbound.rest.mapper;

import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.inbound.rest.dto.GetTransactionQueryDto;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = TransactionRestMapperImpl.class)
class TransactionRestMapperTest {

    @Autowired
    private TransactionRestMapper mapper;

    @Test
    void toSaveTransactionCommand_FromTransactionDto_IsProperlyMapped() {
        TransactionDto transactionDto = TransactionDto.builder().amount(BigDecimal.valueOf(2.92))
                                                      .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                                                      .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                      .note("Locked footage hood duties antarctica bald justin, note.")
                                                      .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();
        SaveTransactionCommand actual = this.mapper.toSaveTransactionCommand("andrei2y", "ada45561-f59d-4e07-8ad0-a8a14749fdce", transactionDto);
        SaveTransactionCommand expected = SaveTransactionCommand.builder().username("andrei2y").id("ada45561-f59d-4e07-8ad0-a8a14749fdce")
                                                                .amount(BigDecimal.valueOf(2.92))
                                                                .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                                                                .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                .note("Locked footage hood duties antarctica bald justin, note.")
                                                                .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        Assertions.assertThat(this.mapper.toSaveTransactionCommand(null, null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toCreateTransactionCommand_FromTransactionDto_IsProperlyMapped() {
        CreateTransactionCommand actual = this.mapper.toCreateTransactionCommand("andrei2y", this.buildTransactionDto());
        CreateTransactionCommand expected = CreateTransactionCommand.builder().username("andrei2y").amount(BigDecimal.valueOf(2.92))
                                                                    .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                                                                    .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                    .note("Locked footage hood duties antarctica bald justin, note.")
                                                                    .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();

        Assertions.assertThat(this.mapper.toCreateTransactionCommand(null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toGetTransactionQuery_FromTransactionFilterDto_IsProperlyMapped() {
        GetTransactionQueryDto queryDto = GetTransactionQueryDto.builder()
                                                                .startDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                .endDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                                .accountNameList(Set.of("Credit Card Account", "Cash"))
                                                                .tagList(Set.of("Syria", "SolarRoad", "Annada"))
                                                                .build();

        GetTransactionQuery actual = this.mapper.toGetTransactionQuery(queryDto, "jinnar1j");
        GetTransactionQuery expected = GetTransactionQuery.builder().username("jinnar1j")
                                                          .startDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                          .endDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                          .accountNameList(Set.of("Credit Card Account", "Cash"))
                                                          .tagValueList(Set.of("Syria", "SolarRoad", "Annada"))
                                                          .build();

        Assertions.assertThat(this.mapper.toGetTransactionQuery(null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toTransactionDtoList_FromTransactionList_IsProperlyMapped() {
        List<Transaction> transactionList = List.of(this.buildTransaction(), this.buildTransaction());

        List<TransactionDto> actual = this.mapper.toTransactionDtoList(transactionList);
        List<TransactionDto> expected = List.of(this.buildTransactionDto(), this.buildTransactionDto());

        Assertions.assertThat(this.mapper.toTransactionDtoList(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    //<editor-fold desc="builders">
    private TransactionDto buildTransactionDto() {
        return TransactionDto.builder()
                             .id("ada45561-f59d-4e07-8ad0-a8a14749fdce").amount(BigDecimal.valueOf(2.92))
                             .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                             .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                             .note("Locked footage hood duties antarctica bald justin, note.")
                             .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();
    }

    private Transaction buildTransaction() {
        return Transaction.builder()
                          .id("ada45561-f59d-4e07-8ad0-a8a14749fdce").amount(BigDecimal.valueOf(2.92))
                          .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                          .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                          .note("Locked footage hood duties antarctica bald justin, note.")
                          .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();
    }
    //</editor-fold>
}