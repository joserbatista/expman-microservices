package com.joserbatista.service.importer.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.importer.outbound.mongo.mapper.TransactionMongoMapper;
import com.joserbatista.service.importer.outbound.mongo.repository.AccountMongoRepository;
import com.joserbatista.service.importer.outbound.mongo.repository.TransactionMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

class ManageTransactionMongoAdapterTest {

    @Mock
    private TransactionMongoMapper mapper;
    @Mock
    private TransactionMongoRepository transactionMongoRepository;
    @Mock
    private AccountMongoRepository accountMongoRepository;
    @InjectMocks
    private ManageTransactionMongoAdapter adapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(this.mapper.toTransactionList(ArgumentMatchers.any()))
               .thenReturn(List.of(Transaction.builder().id("6a6f0786-121d-4f20-8a8c-a91f3ff0a298").build()));
        Mockito.when(this.accountMongoRepository.findAllByUsername("laquetteba"))
               .thenReturn(List.of(AccountDocument.builder().id("232f0234-51f1-41a6-82d2-81b47cd69cea").build()));
    }

    @AfterEach
    void tearDown() throws Exception {
        this.closeable.close();
    }

    @Test
    void createList_ValidParameters_CallsPortMethodOnce() throws BaseException {
        CreateTransactionCommand transaction = CreateTransactionCommand.builder().username("laquetteba")
                                                                       .account(Account.builder().id("232f0234-51f1-41a6-82d2-81b47cd69cea").build())
                                                                       .build();

        this.adapter.createList("laquetteba", List.of(transaction));

        Mockito.verify(this.transactionMongoRepository).insert(ArgumentMatchers.anyCollection());
        Mockito.verify(this.accountMongoRepository).findAllByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

}