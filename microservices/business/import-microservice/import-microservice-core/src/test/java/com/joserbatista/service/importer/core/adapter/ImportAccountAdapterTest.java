package com.joserbatista.service.importer.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.importer.core.adapter.parser.ImportResourceParser;
import com.joserbatista.service.importer.core.api.command.ImportTransactionCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;

class ImportAccountAdapterTest {

    @Mock
    private ImportResourceParser resourceProcessor;
    @Mock
    private ImportFileAccountProcessor accountImport;
    @Mock
    private ImportFileTransactionProcessor transactionProcessor;

    @InjectMocks
    private ImportAccountAdapter adapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.closeable.close();
    }

    @Test
    void importFromFile_ValidParameters_CallsStrategyAndChain() throws Exception {
        ImportTransactionCommand command = ImportTransactionCommand.builder().username("georgeannimox")
                                                                   .resource(Mockito.mock(Resource.class))
                                                                   .contentType("text/csv")
                                                                   .build();
        this.adapter.importFromFile(command);

        Mockito.verify(this.resourceProcessor).process(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.accountImport).process(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.transactionProcessor).process(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void importFromFile_ProcessorThrowsException_DoesNotCallOtherProcessors() throws BaseException {
        Mockito.when(this.resourceProcessor.process(ArgumentMatchers.any(), ArgumentMatchers.any()))
               .thenThrow(FailedValidationException.builder().build());
        ImportTransactionCommand command = ImportTransactionCommand.builder().username("georgeannimox")
                                                                   .resource(Mockito.mock(Resource.class))
                                                                   .contentType("text/csv")
                                                                   .build();
        Assertions.assertThatExceptionOfType(FailedValidationException.class).isThrownBy(() -> this.adapter.importFromFile(command));

        Mockito.verifyNoInteractions(this.accountImport);
        Mockito.verifyNoInteractions(this.transactionProcessor);
    }
}