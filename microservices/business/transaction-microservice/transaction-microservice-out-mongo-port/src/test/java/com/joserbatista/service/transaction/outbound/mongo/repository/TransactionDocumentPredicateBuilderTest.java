package com.joserbatista.service.transaction.outbound.mongo.repository;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Account;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import com.joserbatista.service.transaction.outbound.mongo.document.QTransactionDocument;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

class TransactionDocumentPredicateBuilderTest {

    private final QTransactionDocument document = QTransactionDocument.transactionDocument;
    private TransactionDocumentPredicateBuilder predicateBuilder;

    @BeforeEach
    void setUp() throws BaseException {
        GetAccountRemotePort accountRemotePort = Mockito.mock(GetAccountRemotePort.class);
        Mockito.when(accountRemotePort.findAllByUsername(ArgumentMatchers.any()))
               .thenReturn(List.of(
                   Account.builder().id("ad5f5f2e-bab6-4f96-afea-b17090d30bde").name("Credit Card Account").build(),
                   Account.builder().id("3cf60827-bf1c-417d-abc8-781beb4425bd").name("Cash").build()
               ));
        this.predicateBuilder = new TransactionDocumentPredicateBuilder(accountRemotePort);
    }

    @Test
    void buildPredicateForQuery_AllParameters_IsProperlyMapped() throws BaseException {
        GetTransactionQuery query = GetTransactionQuery.builder().username("jinnar1j")
                                                       .startDate(OffsetDateTime.parse("2022-11-03T10:15:30Z"))
                                                       .endDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                       .accountNameList(Set.of("Credit Card Account", "Cash"))
                                                       .tagValueList(Set.of("Syria", "SolarRoad", "Annada"))
                                                       .build();
        Predicate actual = this.predicateBuilder.buildPredicateForQuery(query);
        StringPath usernamePath = this.document.username;
        Predicate expected = new BooleanBuilder(usernamePath.isNotNull().and(usernamePath.eq("jinnar1j")))
            .and(this.document.accountId.in(Set.of("3cf60827-bf1c-417d-abc8-781beb4425bd", "ad5f5f2e-bab6-4f96-afea-b17090d30bde")))
            .and(this.document.tagList.any().in(Set.of("Syria", "SolarRoad", "Annada")))
            .and(this.document.date.after(OffsetDateTime.parse("2022-11-03T10:15:30Z")))
            .and(this.document.date.before(OffsetDateTime.parse("2022-12-03T10:15:30Z")));

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildPredicateForQuery_OnlyDateParameters_IsProperlyMapped() throws BaseException {
        GetTransactionQuery query = GetTransactionQuery.builder().username("jinnar1j")
                                                       .startDate(OffsetDateTime.parse("2022-11-03T10:15:30Z"))
                                                       .endDate(OffsetDateTime.parse("2022-12-03T10:15:30Z"))
                                                       .build();
        Predicate actual = this.predicateBuilder.buildPredicateForQuery(query);
        StringPath usernamePath = this.document.username;
        Predicate expected = new BooleanBuilder(usernamePath.isNotNull().and(usernamePath.eq("jinnar1j")))
            .and(this.document.date.after(OffsetDateTime.parse("2022-11-03T10:15:30Z")))
            .and(this.document.date.before(OffsetDateTime.parse("2022-12-03T10:15:30Z")));

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}