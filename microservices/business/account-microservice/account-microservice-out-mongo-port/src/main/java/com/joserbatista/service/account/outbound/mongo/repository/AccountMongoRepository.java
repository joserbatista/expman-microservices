package com.joserbatista.service.account.outbound.mongo.repository;

import com.joserbatista.service.account.outbound.mongo.document.AccountDocument;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AccountMongoRepository extends MongoRepository<AccountDocument, String> {

    @Aggregation(pipeline = {
        "{\"$lookup\": {\"from\": \"transactions\",\"localField\": \"id\",\"foreignField\": \"accountId\",\"as\": \"transactions\"}}",
        "{\"$match\": {\"$and\": [{\"username\": \"?0\"}, {\"id\": \"?1\"}]}}",
        "{\"$set\": {\"amount\": {\"$sum\": \"$transactions.amount\"}}}"
    })
    Optional<AccountDocument> findByUsernameAndId(String username, String accountId);

    boolean existsByUsernameAndName(String username, String name);

    @Aggregation(pipeline = {
        "{ \"$lookup\" : { \"from\" : \"transactions\", \"localField\" : \"id\", \"foreignField\" : \"accountId\", \"as\" : \"transactions\"}}",
        "{ \"$match\" : { \"username\" : ?0}}",
        "{ \"$set\" : { \"amount\" : { \"$sum\" : \"$transactions.amount\"}}}"})
    List<AccountDocument> findAllByUsername(String username);
}

