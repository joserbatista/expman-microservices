package com.joserbatista.service.importer.outbound.mongo.repository;

import com.joserbatista.service.importer.outbound.mongo.document.TransactionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, String> {

}

