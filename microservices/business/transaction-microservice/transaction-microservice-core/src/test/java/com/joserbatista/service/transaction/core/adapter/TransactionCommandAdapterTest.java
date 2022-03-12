package com.joserbatista.service.transaction.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import com.joserbatista.service.transaction.core.port.out.TransactionCommandRemotePort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class TransactionCommandAdapterTest {

    @Mock
    private TransactionCommandRemotePort port;
    @Mock
    private GetAccountRemotePort getAccountRemotePort;

    @InjectMocks
    private TransactionCommandAdapter adapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws BaseException {
        this.closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(this.getAccountRemotePort.existsByUsernameAndId("laquetteba", "9c30aa46-924a-410b-8a83-d160e4122640"))
               .thenReturn(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.closeable.close();
    }

    @Test
    void create_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.create(CreateTransactionCommand.builder().username("laquetteba")
                                                    .accountId("9c30aa46-924a-410b-8a83-d160e4122640").build());

        Mockito.verify(this.port).create(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void save_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.save(SaveTransactionCommand.builder().id("2412f038-71d0-4b4c-bac5-35dc86aecffe").username("laquetteba")
                                                .accountId("9c30aa46-924a-410b-8a83-d160e4122640").build());

        Mockito.verify(this.port).save(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void remove_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.remove("laquetteba", "2412f038-71d0-4b4c-bac5-35dc86aecffe");

        Mockito.verify(this.port).remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void save_AccountNotFound_ThrowsFailedValidationException() throws BaseException {
        Mockito.when(this.getAccountRemotePort.existsByUsernameAndId("laquetteba", "9c30aa46-924a-410b-8a83-d160e4122640"))
               .thenReturn(false);

        Assertions.assertThatExceptionOfType(FailedValidationException.class)
                  .isThrownBy(() -> this.adapter.save(SaveTransactionCommand.builder().username("laquetteba")
                                                                            .accountId("9c30aa46-924a-410b-8a83-d160e4122640").build()));

        Mockito.verify(this.getAccountRemotePort).existsByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoInteractions(this.port);
    }

    @Test
    void create_AccountNotFound_ThrowsFailedValidationException() throws BaseException {
        Mockito.when(this.getAccountRemotePort.existsByUsernameAndId("laquetteba", "9c30aa46-924a-410b-8a83-d160e4122640"))
               .thenReturn(false);

        Assertions.assertThatExceptionOfType(FailedValidationException.class)
                  .isThrownBy(() -> this.adapter.create(CreateTransactionCommand.builder().username("laquetteba")
                                                                                .accountId("9c30aa46-924a-410b-8a83-d160e4122640")
                                                                                .build()));

        Mockito.verify(this.getAccountRemotePort).existsByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoInteractions(this.port);
    }

    @Test
    void save_MissingAccountId_ThrowsFailedValidationException() {
        SaveTransactionCommand command = SaveTransactionCommand.builder().username("laquetteba").id("2412f038-71d0-4b4c-bac5-35dc86aecffe").build();
        Assertions.assertThatExceptionOfType(FailedValidationException.class).isThrownBy(() -> this.adapter.save(command));

        Mockito.verifyNoInteractions(this.port);
    }

    @Test
    void create_MissingAccountId_ThrowsFailedValidationException() {
        Assertions.assertThatExceptionOfType(FailedValidationException.class)
                  .isThrownBy(() -> this.adapter.create(CreateTransactionCommand.builder().username("laquetteba").build()));

        Mockito.verifyNoInteractions(this.port);
    }

}