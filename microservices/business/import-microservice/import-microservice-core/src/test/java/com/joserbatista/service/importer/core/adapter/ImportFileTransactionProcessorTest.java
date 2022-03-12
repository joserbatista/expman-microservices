package com.joserbatista.service.importer.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.mapper.TransactionCoreMapper;
import com.joserbatista.service.importer.core.port.out.CreateTransactionPort;
import com.joserbatista.service.importer.core.port.out.GetAccountQueryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

class ImportFileTransactionProcessorTest {

    @Mock
    private GetAccountQueryPort queryPort;
    @Mock
    private TransactionCoreMapper mapper;
    @Mock
    private CreateTransactionPort transactionPort;

    @InjectMocks
    private ImportFileTransactionProcessor processor;

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
    void process_ValidParameters_CallsQueryAndCreatePort() throws BaseException {
        this.processor.process("charquitavk", List.of(CreateTransactionCommand.builder().build()));
        Mockito.verify(this.queryPort).findAllByUsername(ArgumentMatchers.anyString());
        Mockito.verify(this.transactionPort).createList(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }
}