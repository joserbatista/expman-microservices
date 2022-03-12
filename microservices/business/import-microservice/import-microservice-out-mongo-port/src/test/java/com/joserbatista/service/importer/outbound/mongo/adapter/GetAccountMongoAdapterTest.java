package com.joserbatista.service.importer.outbound.mongo.adapter;

import com.joserbatista.service.importer.outbound.mongo.mapper.AccountMongoMapper;
import com.joserbatista.service.importer.outbound.mongo.repository.AccountMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class GetAccountMongoAdapterTest {

    @Mock
    private AccountMongoRepository repository;
    @Mock
    private AccountMongoMapper mapper;

    @InjectMocks
    private GetAccountMongoAdapter adapter;

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
    void findAllByUsername_ValidParameters_CallsRepositoryMethodOnce() {
        this.adapter.findAllByUsername("laquetteba");

        Mockito.verify(this.repository).findAllByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.repository);
    }
}