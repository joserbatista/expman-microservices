package com.joserbatista.service.transaction.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.port.out.GetTransactionRemotePort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class GetTransactionQueryAdapterTest {

    @Mock
    private GetTransactionRemotePort port;

    @InjectMocks
    private GetTransactionQueryAdapter adapter;

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
    void getByFilter_ValidParameters_CallsPortMethodOnce() throws BaseException {
        GetTransactionQuery transactionQuery = GetTransactionQuery.builder().build();

        this.adapter.getByFilter(transactionQuery);

        Mockito.verify(this.port).findByQuery(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void getUserTransactionById_ValidParameters_CallsPortMethodOnce() throws BaseException {
        this.adapter.getUserTransactionById("laquetteba", "8a8a8ab9-c769-487c-be7a-85b6daaafbdd");

        Mockito.verify(this.port).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.port);
    }
}