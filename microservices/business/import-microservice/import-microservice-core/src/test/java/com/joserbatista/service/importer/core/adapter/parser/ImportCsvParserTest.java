package com.joserbatista.service.importer.core.adapter.parser;

import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.common.validation.exception.internal.GenericInternalErrorException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.mapper.TransactionCoreMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;

class ImportCsvParserTest {

    private ImportCsvParser importAccountFileCsvStrategy;
    private Resource resource;

    @BeforeEach
    void setUp() {
        this.importAccountFileCsvStrategy = new ImportCsvParser(Mappers.getMapper(TransactionCoreMapper.class));
        this.resource = Mockito.mock(Resource.class);
        Mockito.when(this.resource.getFilename()).thenReturn("mock.csv");
    }

    @Test
    void handle_ValidResource_DoesNotThrow() throws Exception {
        this.mockInputStreamWithBody("date;account;tags;amount;note\n2021-08-31T19:20:22Z;Credit Card;Government|Tax;54.59;Car Tax");

        List<CreateTransactionCommand> actual = this.importAccountFileCsvStrategy.parse("aamilyixu", this.resource);
        List<CreateTransactionCommand> expected = List.of(
            CreateTransactionCommand.builder()
                                    .username("aamilyixu")
                                    .date(OffsetDateTime.parse("2021-08-31T19:20:22Z")).account(Account.builder().name("Credit Card").build())
                                    .tagList(List.of("Government", "Tax")).note("Car Tax")
                                    .amount(BigDecimal.valueOf(54.59))
                                    .build()
        );

        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void handle_InvalidResourceHeader_ThrowsFailedValidationException() throws Exception {
        this.mockInputStreamWithBody("date;tags;account;amount;note\n2021-08-31T19:20:22Z;Government|Tax;Credit Card;54.59;Car Tax");

        Assertions.assertThatExceptionOfType(FailedValidationException.class)
                  .isThrownBy(() -> this.importAccountFileCsvStrategy.parse("aamilyixu", this.resource));
    }

    @Test
    void handle_IoException_ThrowsFailedValidationException() throws Exception {
        Mockito.when(this.resource.getInputStream()).thenThrow(new IOException("Mock"));

        Assertions.assertThatExceptionOfType(GenericInternalErrorException.class)
                  .isThrownBy(() -> this.importAccountFileCsvStrategy.parse("aamilyixu", this.resource));
    }

    private void mockInputStreamWithBody(String body) throws IOException {
        Mockito.when(this.resource.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
    }
}