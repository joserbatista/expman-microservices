package com.joserbatista.service.transaction.core.adapter.group;

import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

class GroupByTagHandlerTest {

    private GroupByTagHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new GroupByTagHandler();
    }

    @Test
    void group_ValidQuery_ReturnsExpectedElements() {
        GetTransactionQuery query = this.getGetTransactionQuery();
        List<Transaction> transactionList = this.getTransactionList();
        Group actual = this.handler.group(query, transactionList);
        Group expected = Group.builder().by(Group.By.TAG).startDate(query.getStartDate()).endDate(query.getEndDate())
                              .elementList(List.of(
                                  Group.Element.builder().transactionCount(3).value("Rent")
                                               .totalAmount(BigDecimal.valueOf(1800.00))
                                               .idList(List.of("9bee7ed7", "6e4572ec", "ba36e720"))
                                               .build(),
                                  Group.Element.builder().transactionCount(2).value("Dinner")
                                               .totalAmount(BigDecimal.valueOf(31.24))
                                               .idList(List.of("cd5e66ee", "3c2cd5dc"))
                                               .build()
                              ))
                              .build();

        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    private List<Transaction> getTransactionList() {
        return List.of(
            Transaction.builder().id("3c2cd5dc").amount(BigDecimal.valueOf(25.99)).tagList(Set.of("Dinner", "Pizza")).build(),
            Transaction.builder().id("cd5e66ee").amount(BigDecimal.valueOf(5.25)).tagList(Set.of("Dinner")).build(),
            Transaction.builder().id("c06a8183").amount(BigDecimal.valueOf(6.99)).tagList(Set.of("Lunch")).build(),
            Transaction.builder().id("9bee7ed7").amount(BigDecimal.valueOf(600.00)).tagList(Set.of("Rent")).build(),
            Transaction.builder().id("6e4572ec").amount(BigDecimal.valueOf(600.00)).tagList(Set.of("Rent")).build(),
            Transaction.builder().id("ba36e720").amount(BigDecimal.valueOf(600.00)).tagList(Set.of("Rent")).build(),
            Transaction.builder().id("acc0b7fc").amount(BigDecimal.valueOf(45.99)).tagList(Set.of("Dentist")).build()
        );
    }

    private GetTransactionQuery getGetTransactionQuery() {
        return GetTransactionQuery.builder().username("jinnar1j")
                                  .startDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                  .endDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                  .tagValueList(Set.of("Dinner", "Rent"))
                                  .build();
    }
}