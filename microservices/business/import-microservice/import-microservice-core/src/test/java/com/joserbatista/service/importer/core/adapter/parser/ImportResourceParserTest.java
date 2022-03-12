package com.joserbatista.service.importer.core.adapter.parser;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.importer.core.api.command.ImportTransactionCommand;
import com.joserbatista.service.importer.core.mapper.TransactionCoreMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;

@SpringBootTest(classes = {ImportCsvParser.class, ImportResourceParser.class})
@MockBean(TransactionCoreMapper.class)
class ImportResourceParserTest {

    @SpyBean
    private ImportResourceParser strategyFactory;

    @Test
    void create_CsvExtension_ReturnsImportAccountFileCsvStrategy() throws BaseException {
        ImportResourceParser.Parser parser = this.strategyFactory.createByExtension("csv");

        Assertions.assertThat(parser).isInstanceOf(ImportCsvParser.class);
    }

    @Test
    void handle_CsvResource_CallsStrategyOnce() throws BaseException {
        ImportTransactionCommand command = ImportTransactionCommand.builder().username("username")
                                                                   .resource(Mockito.mock(Resource.class))
                                                                   .contentType("text/csv")
                                                                   .build();

        ImportResourceParser.Parser parserMock = Mockito.mock(ImportResourceParser.Parser.class);

        Mockito.doReturn(parserMock).when(this.strategyFactory).createByExtension(ArgumentMatchers.any());
        this.strategyFactory.process(command.getResource(), command.getUsername());

        Mockito.verify(parserMock).parse(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void create_UnsupportedExtension_ThrowsFailedValidationException() {
        Assertions.assertThatExceptionOfType(FailedValidationException.class).isThrownBy(() -> this.strategyFactory.createByExtension("xml"));
    }
}