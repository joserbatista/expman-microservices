package com.joserbatista.service.account.outbound.mongo.adapter;

import com.joserbatista.service.account.outbound.mongo.mapper.AccountMongoMapper;
import com.joserbatista.service.account.outbound.mongo.repository.AccountMongoRepository;
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
    void findByUsernameAndId_ValidParameters_CallsRepositoryMethodOnce() {
        this.adapter.findByUsernameAndId("laquetteba", "806b8367-4586-454c-8ac7-f34b5a4830b8");

        Mockito.verify(this.repository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.repository);
    }

    @Test
    void findByUsername_ValidParameters_CallsRepositoryMethodOnce() {
        this.adapter.findByUsername("laquetteba");

        Mockito.verify(this.repository).findAllByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.repository);
    }

    @Test
    void existsByUserUsernameAndNameIn_ValidParameters_CallsRepositoryMethodOnce() {
        this.adapter.existsByUserUsernameAndNameIn("laquetteba", "Credit Card");

        Mockito.verify(this.repository).existsByUsernameAndName(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.repository);
    }
}