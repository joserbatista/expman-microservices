package com.joserbatista.service.transaction.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.transaction.core.adapter.group.GroupByAccountHandler;
import com.joserbatista.service.transaction.core.adapter.group.GroupByTagHandler;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.in.GetTransactionQueryPort;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@SpringBootTest(classes = {GroupTransactionQueryAdapter.class, GroupByAccountHandler.class, GroupByTagHandler.class})
@MockBean(classes = GetAccountRemotePort.class)
class GroupTransactionQueryAdapterTest {

    @MockBean
    private GetTransactionQueryPort port;

    @Autowired
    private GroupTransactionQueryAdapter adapter;

    @SpyBean
    private GroupByAccountHandler accountHandler;
    @SpyBean
    private GroupByTagHandler tagHandler;

    @BeforeEach
    void setUp() throws BaseException {
        Mockito.when(this.port.getByFilter(ArgumentMatchers.any())).thenReturn(List.of(Transaction.builder().build()));
    }

    @Test
    void byFilter_ByTag_CallsExpectedHandleMethod() throws BaseException {
        this.adapter.byFilter(GetTransactionQuery.builder().build(), Group.By.TAG);
        Mockito.verify(this.tagHandler).group(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoInteractions(this.accountHandler);
    }

    @Test
    void byFilter_ByAccount_CallsExpectedHandleMethod() throws BaseException {
        this.adapter.byFilter(GetTransactionQuery.builder().build(), Group.By.ACCOUNT);
        Mockito.verify(this.accountHandler).group(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoInteractions(this.tagHandler);
    }

    @Test
    void byFilter_NullBy_ThrowsException() {
        Assertions.assertThrows(FailedValidationException.class, () -> this.adapter.byFilter(GetTransactionQuery.builder().build(), null));
        Mockito.verifyNoInteractions(this.accountHandler);
        Mockito.verifyNoInteractions(this.tagHandler);
    }

}