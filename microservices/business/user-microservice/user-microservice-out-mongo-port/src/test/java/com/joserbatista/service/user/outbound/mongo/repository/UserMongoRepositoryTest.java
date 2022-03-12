package com.joserbatista.service.user.outbound.mongo.repository;

import com.joserbatista.service.user.outbound.mongo.MongoConfig;
import com.joserbatista.service.user.outbound.mongo.document.UserDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

@DataMongoTest
@ActiveProfiles("test")
@ContextConfiguration(classes = MongoConfig.class)
class UserMongoRepositoryTest {

    @Autowired
    private UserMongoRepository repository;

    @BeforeEach
    void setUp() {
        this.repository.saveAll(List.of(
            UserDocument.builder().username("drapera").email("cystal_harlowlm@jon.ozw").fullName("Dove Servin").build(),
            UserDocument.builder().username("obadiahu1").email("teonia_bazemore1@genesis.jlu").fullName("Eldridge Renick").build(),
            UserDocument.builder().username("maryax").email("wanda_sweetvxkx@http.ba").fullName("Garett Wachter").build(),
            UserDocument.builder().username("samiaaf").email("peterson_sebringiqt@combinations.id").fullName("Marnee Beale").build()
        ));
    }

    @Test
    void findByUsername_ExistingUser_ReturnsDocument() {
        Optional<UserDocument> userDocumentOptional = this.repository.findByUsername("drapera");

        Assertions.assertThat(userDocumentOptional).isPresent();
        Assertions.assertThat(userDocumentOptional.get().getId()).isNotNull();
    }

    @Test
    void findByUsername_NonExistingUser_ReturnsEmpty() {
        Optional<UserDocument> userDocumentOptional = this.repository.findByUsername("hakeem");

        Assertions.assertThat(userDocumentOptional).isEmpty();
    }

    @Test
    void existsByUsername_NonExistingUser_ReturnsFalse() {
        boolean existsByUsername = this.repository.existsByUsername("drapera");

        Assertions.assertThat(existsByUsername).isTrue();
    }

    @Test
    void existsByUsername_ExistingUser_ReturnsTrue() {
        boolean existsByUsername = this.repository.existsByUsername("hakeem");

        Assertions.assertThat(existsByUsername).isFalse();
    }

}