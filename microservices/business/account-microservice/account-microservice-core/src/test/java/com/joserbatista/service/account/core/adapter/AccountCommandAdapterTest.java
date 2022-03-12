package com.joserbatista.service.account.core.adapter;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.port.out.AccountCommandRemotePort;
import com.joserbatista.service.common.validation.exception.BaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class AccountCommandAdapterTest {

    @Mock
    private AccountCommandRemotePort port;

    @InjectMocks
    private AccountCommandAdapter adapter;

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
    void create_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.create(CreateAccountCommand.builder().build());

        Mockito.verify(this.port).create(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void remove_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.remove("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63");

        Mockito.verify(this.port).remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void save_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.save(SaveAccountCommand.builder().id("5cc0e481-d819-49e7-8ee1-347dc08e9e63").username("laquetteba").build());

        Mockito.verify(this.port).save(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

}