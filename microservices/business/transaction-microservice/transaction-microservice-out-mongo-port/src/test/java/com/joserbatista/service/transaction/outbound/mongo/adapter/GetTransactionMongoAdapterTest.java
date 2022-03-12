package com.joserbatista.service.transaction.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.outbound.mongo.mapper.TransactionMongoMapper;
import com.joserbatista.service.transaction.outbound.mongo.repository.TransactionDocumentPredicateBuilder;
import com.joserbatista.service.transaction.outbound.mongo.repository.TransactionMongoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class GetTransactionMongoAdapterTest {

    @Mock
    private TransactionMongoRepository transactionMongoRepository;
    @Mock
    private TransactionMongoMapper mapper;
    @Mock
    private TransactionDocumentPredicateBuilder predicateBuilder;

    @InjectMocks
    private GetTransactionMongoAdapter adapter;

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
    void findByQuery_ValidParameters_CallsPortMethodOnce() throws BaseException {
        GetTransactionQuery transactionQuery = GetTransactionQuery.builder().build();
        Mockito.when(this.predicateBuilder.buildPredicateForQuery(ArgumentMatchers.any())).thenReturn(new BooleanBuilder());

        this.adapter.findByQuery(transactionQuery);

        Mockito.verify(this.transactionMongoRepository).findAll(ArgumentMatchers.any(Predicate.class));
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

    @Test
    void findByUsernameAndId_ValidParameters_CallsPortMethodOnce() {
        this.adapter.findByUsernameAndId("laquetteba", "8a8a8ab9-c769-487c-be7a-85b6daaafbdd");

        Mockito.verify(this.transactionMongoRepository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.transactionMongoRepository);
    }

}