package com.joserbatista.service.transaction.outbound.mongo.repository;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Account;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import com.joserbatista.service.transaction.outbound.mongo.document.QTransactionDocument;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class TransactionDocumentPredicateBuilder {

    private GetAccountRemotePort getAccountRemotePort;

    public Predicate buildPredicateForQuery(GetTransactionQuery getTransactionQuery) throws BaseException {
        QTransactionDocument transactionDocument = QTransactionDocument.transactionDocument;

        StringPath usernamePath = transactionDocument.username;
        BooleanBuilder booleanBuilder = new BooleanBuilder(usernamePath.isNotNull().and(usernamePath.eq(getTransactionQuery.getUsername())));

        if (!CollectionUtils.isEmpty(getTransactionQuery.getAccountNameList())) {
            booleanBuilder.and(transactionDocument.accountId.in(this.fillAccountNameList(getTransactionQuery)));
        }
        if (!CollectionUtils.isEmpty(getTransactionQuery.getTagValueList())) {
            booleanBuilder.and(transactionDocument.tagList.any().in(getTransactionQuery.getTagValueList()));
        }
        if (getTransactionQuery.getStartDate() != null) {
            booleanBuilder.and(transactionDocument.date.after(getTransactionQuery.getStartDate()));
        }
        if (getTransactionQuery.getEndDate() != null) {
            booleanBuilder.and(transactionDocument.date.before(getTransactionQuery.getEndDate()));
        }

        log.trace("Created Predicate {}", booleanBuilder);

        return booleanBuilder;
    }

    private Set<String> fillAccountNameList(GetTransactionQuery getTransactionQuery) throws BaseException {
        Set<String> accountNameList = getTransactionQuery.getAccountNameList();
        log.trace("Finding Account.id for Account.name={}", accountNameList);
        List<Account> accountDocumentList = this.getAccountRemotePort.findAllByUsername(getTransactionQuery.getUsername());

        return accountDocumentList.stream().filter(accountDocument -> accountNameList.contains(accountDocument.getName()))
                                  .map(Account::getId).collect(Collectors.toSet());
    }
}
