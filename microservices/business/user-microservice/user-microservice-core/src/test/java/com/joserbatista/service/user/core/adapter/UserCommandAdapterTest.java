package com.joserbatista.service.user.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.DuplicatedResourceException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.port.in.GetUserQueryPort;
import com.joserbatista.service.user.core.port.out.UserCommandRemotePort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class UserCommandAdapterTest {

    @Mock
    private UserCommandRemotePort commandRemotePort;
    @Mock
    private GetUserQueryPort queryPort;

    @InjectMocks
    private UserCommandAdapter adapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(this.queryPort.existsByUsername(ArgumentMatchers.any())).thenReturn(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.closeable.close();
    }

    @Test
    void create_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.queryPort.existsByUsername(ArgumentMatchers.any())).thenReturn(false);
        this.adapter.create(CreateUserCommand.builder().build());

        Mockito.verify(this.commandRemotePort).create(ArgumentMatchers.any());
        Mockito.verify(this.queryPort).existsByUsername(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.commandRemotePort);
        Mockito.verifyNoMoreInteractions(this.queryPort);
    }

    @Test
    void create_ExistingUser_ThrowsException() {
        Assertions.assertThatExceptionOfType(DuplicatedResourceException.class)
                  .isThrownBy(() -> this.adapter.create(CreateUserCommand.builder().build()));

        Mockito.verify(this.queryPort).existsByUsername(ArgumentMatchers.any());
        Mockito.verifyNoInteractions(this.commandRemotePort);
        Mockito.verifyNoMoreInteractions(this.queryPort);
    }

    @Test
    void removeByUsername_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.removeByUsername("laquetteba");

        Mockito.verify(this.commandRemotePort).removeByUsername(ArgumentMatchers.any());
        Mockito.verify(this.queryPort).existsByUsername(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.commandRemotePort);
        Mockito.verifyNoMoreInteractions(this.queryPort);
    }

    @Test
    void removeByUsername_NonExisting_ThrowsException() {
        Mockito.when(this.queryPort.existsByUsername(ArgumentMatchers.any())).thenReturn(false);

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> this.adapter.removeByUsername("laquetteba"));

        Mockito.verify(this.queryPort).existsByUsername(ArgumentMatchers.any());
        Mockito.verifyNoInteractions(this.commandRemotePort);
        Mockito.verifyNoMoreInteractions(this.queryPort);
    }

    @Test
    void save_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.save(SaveUserCommand.builder().build());

        Mockito.verify(this.commandRemotePort).save(ArgumentMatchers.any());
        Mockito.verify(this.queryPort).existsByUsername(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.commandRemotePort);
        Mockito.verifyNoMoreInteractions(this.queryPort);
    }

    @Test
    void save_NonExisting_ThrowsException() {
        Mockito.when(this.queryPort.existsByUsername(ArgumentMatchers.any())).thenReturn(false);

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> this.adapter.removeByUsername("laquetteba"));

        Mockito.verify(this.queryPort).existsByUsername(ArgumentMatchers.any());
        Mockito.verifyNoInteractions(this.commandRemotePort);
        Mockito.verifyNoMoreInteractions(this.queryPort);
    }
}