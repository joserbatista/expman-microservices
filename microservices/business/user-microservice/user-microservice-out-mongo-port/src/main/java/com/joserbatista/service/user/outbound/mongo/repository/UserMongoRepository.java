package com.joserbatista.service.user.outbound.mongo.repository;

import com.joserbatista.service.user.outbound.mongo.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByUsername(String username);

    boolean existsByUsername(String username);

}

