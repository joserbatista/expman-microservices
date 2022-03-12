package com.joserbatista.service.transaction.core.adapter.group;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Account;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

class GroupByAccountHandlerTest {

    @Mock
    private GetAccountRemotePort accountRemotePort;
    @InjectMocks
    private GroupByAccountHandler handler;

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
    void group_ValidQuery_ReturnsExpectedElements() throws BaseException {
        Mockito.when(this.accountRemotePort.findAllByUsername(ArgumentMatchers.any())).thenReturn(List.of(
            Account.builder().id("be2c0735").name("Bank Account").build(),
            Account.builder().id("d5ea642c").name("Credit Card").build(),
            Account.builder().id("29d61f81").name("Debit Card").build())
        );

        GetTransactionQuery query = this.getGetTransactionQuery();
        List<Transaction> transactionList = this.getTransactionList();
        Group actual = this.handler.group(query, transactionList);
        Group expected = Group.builder()
                              .by(Group.By.ACCOUNT).startDate(query.getStartDate()).endDate(query.getEndDate())
                              .elementList(List.of(
                                  Group.Element.builder().transactionCount(3).value("Bank Account")
                                               .totalAmount(BigDecimal.valueOf(1800.00))
                                               .idList(List.of("9bee7ed7", "6e4572ec", "ba36e720"))
                                               .build(),
                                  Group.Element.builder().transactionCount(2).value("Credit Card")
                                               .totalAmount(BigDecimal.valueOf(31.24))
                                               .idList(List.of("cd5e66ee", "3c2cd5dc"))
                                               .build()
                              ))
                              .build();

        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    private List<Transaction> getTransactionList() {
        return List.of(
            Transaction.builder().id("3c2cd5dc").amount(BigDecimal.valueOf(25.99)).accountId("d5ea642c").build(),
            Transaction.builder().id("9bee7ed7").amount(BigDecimal.valueOf(600.00)).accountId("be2c0735").build(),
            Transaction.builder().id("cd5e66ee").amount(BigDecimal.valueOf(5.25)).accountId("d5ea642c").build(),
            Transaction.builder().id("ba36e720").amount(BigDecimal.valueOf(600.00)).accountId("be2c0735").build(),
            Transaction.builder().id("c06a8183").amount(BigDecimal.valueOf(6.99)).accountId("29d61f81").build(),
            Transaction.builder().id("6e4572ec").amount(BigDecimal.valueOf(600.00)).accountId("be2c0735").build(),
            Transaction.builder().id("acc0b7fc").amount(BigDecimal.valueOf(45.99)).accountId("29d61f81").build()
        );
    }

    private GetTransactionQuery getGetTransactionQuery() {
        return GetTransactionQuery.builder().username("jinnar1j")
                                  .startDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                  .endDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                  .accountNameList(Set.of("Bank Account", "Credit Card"))
                                  .build();
    }
}