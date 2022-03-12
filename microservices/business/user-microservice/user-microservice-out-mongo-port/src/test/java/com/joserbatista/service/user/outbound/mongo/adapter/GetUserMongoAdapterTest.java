package com.joserbatista.service.user.outbound.mongo.adapter;

import com.joserbatista.service.user.outbound.mongo.document.UserDocument;
import com.joserbatista.service.user.outbound.mongo.mapper.UserMongoMapper;
import com.joserbatista.service.user.outbound.mongo.repository.UserMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class GetUserMongoAdapterTest {

    @Mock
    private UserMongoRepository repository;

    @Mock
    private UserMongoMapper mapper;

    @InjectMocks
    private GetUserMongoAdapter adapter;

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
    void findByUsername_ValidParameters_CallsPortMethodOnce() {
        Mockito.when(this.repository.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.of(UserDocument.builder().build()));

        this.adapter.findByUsername("laquetteba");

        Mockito.verify(this.repository).findByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.repository);
    }

    @Test
    void existsByUsername_ValidParameters_CallsPortMethodOnce() {
        this.adapter.existsByUsername("laquetteba");

        Mockito.verify(this.repository).existsByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.repository);
    }
}