package com.joserbatista.service.user.core.adapter;

import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.out.GetUserRemotePort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class GetUserQueryAdapterTest {

    @Mock
    private GetUserRemotePort port;

    @InjectMocks
    private GetUserQueryAdapter adapter;

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
    void getByUsername_ValidParameters_CallsPortMethodOnce() {
        Mockito.when(this.port.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.of(User.builder().build()));

        this.adapter.getByUsername("laquetteba");

        Mockito.verify(this.port).findByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void existsByUsername_ValidParameters_CallsPortMethodOnce() {
        this.adapter.existsByUsername("laquetteba");

        Mockito.verify(this.port).existsByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.port);
    }
}