package com.joserbatista.service.importer.inbound.rest.mapper;

import com.joserbatista.service.importer.core.api.command.ImportTransactionCommand;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.inbound.rest.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@SpringBootTest(classes = ImportRestMapperImpl.class)
class ImportRestMapperTest {

    @Autowired
    private ImportRestMapper mapper;

    @Test
    void toTransactionDtoList_FromTransactionList_IsProperlyMapped() {
        List<Transaction> transactionList = List.of(this.buildTransaction(), this.buildTransaction());

        List<TransactionDto> actual = this.mapper.toTransactionDtoList(transactionList);
        List<TransactionDto> expected = List.of(this.buildTransactionDto(), this.buildTransactionDto());

        Assertions.assertThat(this.mapper.toTransactionDtoList(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toImportTransactionCommand_FromUsernameAndMultipartFile_IsProperlyMapped() {
        MultipartFile file = Mockito.mock(MultipartFile.class, Mockito.RETURNS_DEEP_STUBS);
        ImportTransactionCommand actual = this.mapper.toImportTransactionCommand("jinnar1j", file);
        ImportTransactionCommand expected = ImportTransactionCommand.builder()
                                                                    .resource(file.getResource())
                                                                    .contentType(file.getContentType())
                                                                    .username("jinnar1j")
                                                                    .build();

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
                             .tagList(List.of("Syria", "SolarRoad", "Annada")).build();
    }

    private Transaction buildTransaction() {
        return Transaction.builder()
                          .id("ada45561-f59d-4e07-8ad0-a8a14749fdce").amount(BigDecimal.valueOf(2.92))
                          .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                          .date(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                          .note("Locked footage hood duties antarctica bald justin, note.")
                          .tagList(List.of("Syria", "SolarRoad", "Annada")).build();
    }
    //</editor-fold>
}