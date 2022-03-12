package com.joserbatista.service.importer.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.mapper.TransactionCoreMapper;
import com.joserbatista.service.importer.core.port.out.CreateAccountPort;
import com.joserbatista.service.importer.core.port.out.GetAccountQueryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

class ImportFileAccountProcessorTest {

    @Mock
    private CreateAccountPort accountPort;
    @Mock
    private GetAccountQueryPort queryPort;
    @Spy
    private TransactionCoreMapper transactionCoreMapper;

    @InjectMocks
    private ImportFileAccountProcessor accountProcessor;

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
    void process_NoAccountsToCreate_DoesNotCallPort() throws BaseException {
        List<CreateTransactionCommand> commandList = List.of(
            CreateTransactionCommand.builder().account(Account.builder().name("Credit Card").build()).build(),
            CreateTransactionCommand.builder().account(Account.builder().name("Debit Card").build()).build()
        );

        Mockito.when(this.queryPort.findAllByUsername(ArgumentMatchers.any())).thenReturn(List.of(
            Account.builder().name("Credit Card").build(), Account.builder().name("Debit Card").build()
        ));
        this.accountProcessor.process("markia6j", commandList);

        Mockito.verify(this.queryPort, Mockito.times(1)).findAllByUsername(ArgumentMatchers.any());
        Mockito.verifyNoInteractions(this.accountPort);
    }

    @Test
    void process_TwoAccountsToCreate_CallsPort() throws BaseException {
        List<CreateTransactionCommand> commandList = List.of(
            CreateTransactionCommand.builder().account(Account.builder().name("Credit Card").build()).build(),
            CreateTransactionCommand.builder().account(Account.builder().name("Debit Card").build()).build(),
            CreateTransactionCommand.builder().account(Account.builder().name("PayPal").build()).build()
        );

        Mockito.when(this.queryPort.findAllByUsername(ArgumentMatchers.any())).thenReturn(List.of(Account.builder().name("PayPal").build()));

        this.accountProcessor.process("markia6j", commandList);

        Mockito.verify(this.queryPort, Mockito.times(1)).findAllByUsername(ArgumentMatchers.any());
        Mockito.verify(this.accountPort).createList(ArgumentMatchers.any(),
                                                    ArgumentMatchers.argThat(accountsToCreateList -> 2 == accountsToCreateList.size()));
    }
}