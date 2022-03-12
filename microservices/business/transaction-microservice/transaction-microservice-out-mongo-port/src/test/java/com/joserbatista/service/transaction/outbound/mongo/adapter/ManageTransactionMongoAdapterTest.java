package com.joserbatista.service.transaction.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.outbound.mongo.document.TransactionDocument;
import com.joserbatista.service.transaction.outbound.mongo.mapper.TransactionMongoMapper;
import com.joserbatista.service.transaction.outbound.mongo.repository.TransactionMongoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class ManageTransactionMongoAdapterTest {

    @Mock
    private TransactionMongoMapper mapper;
    @Mock
    private TransactionMongoRepository transactionMongoRepository;

    @InjectMocks
    private ManageTransactionMongoAdapter adapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(this.mapper.toTransactionList(ArgumentMatchers.any()))
               .thenReturn(List.of(Transaction.builder().id("6a6f0786-121d-4f20-8a8c-a91f3ff0a298").build()));
    }

    @AfterEach
    void tearDown() throws Exception {
        this.closeable.close();
    }

    @Test
    void create_ValidParameters_CallsPortMethodOnce() throws BaseException {
        CreateTransactionCommand transaction = CreateTransactionCommand.builder().username("laquetteba")
                                                                       .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                                                                       .build();
        Mockito.when(this.transactionMongoRepository.insert((TransactionDocument) ArgumentMatchers.any()))
               .thenReturn(TransactionDocument.builder().id("5cc0e481-d819-49e7-8ee1-347dc08e9e63").build());

        this.adapter.create(transaction);

        Mockito.verify(this.transactionMongoRepository).insert((TransactionDocument) ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

    @Test
    void remove_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.transactionMongoRepository.findByUsernameAndId("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63"))
               .thenReturn(Optional.of(TransactionDocument.builder().documentId("").build()));

        this.adapter.remove("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63");

        Mockito.verify(this.transactionMongoRepository).deleteById(ArgumentMatchers.anyString());
        Mockito.verify(this.transactionMongoRepository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

    @Test
    void save_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.transactionMongoRepository.findByUsernameAndId("laquetteba", "9c30aa46-924a-410b-8a83-d160e4122640"))
               .thenReturn(Optional.of(TransactionDocument.builder().documentId("").build()));

        this.adapter.save(this.buildSaveTransactionCommand());

        Mockito.verify(this.transactionMongoRepository).findByUsernameAndId(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.transactionMongoRepository).save(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

    @Test
    void remove_TransactionNotFound_ThrowsResourceNotFoundException() {
        Mockito.when(this.transactionMongoRepository.findByUsernameAndId("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63"))
               .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                  .isThrownBy(() -> this.adapter.remove("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63"));

        Mockito.verify(this.transactionMongoRepository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

    @Test
    void save_TransactionNotFound_ThrowsResourceNotFoundException() {
        Mockito.when(this.transactionMongoRepository.findByUsernameAndId("laquetteba", "9c30aa46-924a-410b-8a83-d160e4122640"))
               .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                  .isThrownBy(() -> this.adapter.save(this.buildSaveTransactionCommand()));

        Mockito.verify(this.transactionMongoRepository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

    private SaveTransactionCommand buildSaveTransactionCommand() {
        return SaveTransactionCommand.builder().username("laquetteba")
                                     .id("9c30aa46-924a-410b-8a83-d160e4122640")
                                     .accountId("232f0234-51f1-41a6-82d2-81b47cd69cea")
                                     .build();
    }
}