package com.joserbatista.service.account.core.adapter;

import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.account.core.port.out.GetAccountRemotePort;
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

class GetAccountQueryAdapterTest {

    @Mock
    private GetAccountRemotePort port;

    @InjectMocks
    private GetAccountQueryAdapter adapter;

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
    void getAccountTypes_NoParameters_DoesNotCallPort() {
        List<AccountType> accountTypeList = this.adapter.getAccountTypes();
        Assertions.assertThat(accountTypeList).isNotNull();
        Mockito.verifyNoInteractions(this.port);
    }

    @Test
    void getAccountById_ValidParameters_CallsPortMethodOnce() {
        this.adapter.getAccountById("laquetteba", "8a8a8ab9-c769-487c-be7a-85b6daaafbdd");

        Mockito.verify(this.port).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void getAccountListByUsername_ValidParameters_CallsPortMethodOnce() {
        this.adapter.getAccountList("laquetteba");

        Mockito.verify(this.port).findByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.port);
    }
}