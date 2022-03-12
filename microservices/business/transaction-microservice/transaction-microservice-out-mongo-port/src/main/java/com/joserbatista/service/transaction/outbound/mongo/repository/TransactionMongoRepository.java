package com.joserbatista.service.transaction.outbound.mongo.repository;

import com.joserbatista.service.transaction.outbound.mongo.document.TransactionDocument;
import com.querydsl.core.types.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, String>, QuerydslPredicateExecutor<TransactionDocument> {

    Optional<TransactionDocument> findByUsernameAndId(String username, String id);

    List<TransactionDocument> findAllByUsername(String username);

    @Override
    List<TransactionDocument> findAll(Predicate predicate);
}

