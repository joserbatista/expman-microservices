package com.joserbatista.service.importer.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateAccountCommand;
import com.joserbatista.service.importer.core.domain.Account;
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

import java.util.List;

class ManageAccountMongoAdapterTest {

    @Mock
    private AccountMongoMapper mapper;
    @Mock
    private AccountMongoRepository accountMongoRepository;

    @InjectMocks
    private ManageAccountMongoAdapter adapter;

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
    void createList_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.accountMongoRepository.existsByUsernameAndName("laquetteba", "Credit Account")).thenReturn(false);
        Mockito.when(this.mapper.toAccountList(ArgumentMatchers.any())).thenReturn(List.of(this.buildAccount()));

        this.adapter.createList("laquetteba", List.of(CreateAccountCommand.builder().username("laquetteba").name("Credit Account").build()));

        Mockito.verify(this.accountMongoRepository).existsByUsernameAndName(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.accountMongoRepository).saveAll(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    private Account buildAccount() {
        return Account.builder().id("9c30aa46-924a-410b-8a83-d160e4122640").name("Credit Account").build();
    }
}