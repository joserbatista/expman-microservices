package com.joserbatista.service.importer.outbound.mongo.repository;

import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountMongoRepository extends MongoRepository<AccountDocument, String> {

    boolean existsByUsernameAndName(String username, String name);

    List<AccountDocument> findAllByUsername(String username);
}