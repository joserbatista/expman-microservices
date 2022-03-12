package com.joserbatista.service.transaction.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.out.GetTransactionRemotePort;
import com.joserbatista.service.transaction.outbound.mongo.document.TransactionDocument;
import com.joserbatista.service.transaction.outbound.mongo.mapper.TransactionMongoMapper;
import com.joserbatista.service.transaction.outbound.mongo.repository.TransactionDocumentPredicateBuilder;
import com.joserbatista.service.transaction.outbound.mongo.repository.TransactionMongoRepository;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GetTransactionMongoAdapter implements GetTransactionRemotePort {

    private final TransactionMongoRepository transactionMongoRepository;
    private final TransactionMongoMapper mapper;
    private final TransactionDocumentPredicateBuilder predicateBuilder;

    @Override
    public Optional<Transaction> findByUsernameAndId(String username, String id) {
        log.debug("Finding Transaction.id={} for username={}", id, username);
        return this.transactionMongoRepository.findByUsernameAndId(username, id).map(this.mapper::toTransaction);
    }

    @Override
    public List<Transaction> findByQuery(GetTransactionQuery getTransactionQuery) throws BaseException {
        log.debug("Finding Transactions for query={}", getTransactionQuery);

        Predicate predicate = this.predicateBuilder.buildPredicateForQuery(getTransactionQuery);
        List<TransactionDocument> transactionDocumentList = this.transactionMongoRepository.findAll(predicate);

        log.trace("Found {} Transactions", transactionDocumentList.size());
        return this.mapper.toTransactionList(transactionDocumentList);
    }
}
