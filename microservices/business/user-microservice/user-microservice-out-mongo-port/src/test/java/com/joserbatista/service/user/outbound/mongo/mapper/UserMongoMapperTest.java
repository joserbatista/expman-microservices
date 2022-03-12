package com.joserbatista.service.user.outbound.mongo.mapper;

import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.outbound.mongo.document.UserDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserMongoMapperImpl.class)
class UserMongoMapperTest {

    @Autowired
    private UserMongoMapper mapper;

    @Test
    void toUser_FromUserDocument_IsProperlyMapped() {
        UserDocument userDocument = UserDocument.builder().id("5b17c78e-bf9a-4293-be5a-e16da9ba8341").username("Mikelle")
                                                .email("cystal_harlowlm@jon.ozw").fullName("Dove Servin").build();
        User actual = this.mapper.toUser(userDocument);
        User expected = this.buildUser();

        Assertions.assertThat(this.mapper.toUser(null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toUserDocument_FromSaveUserCommand_IsProperlyMapped() {
        SaveUserCommand command = SaveUserCommand.builder().username("Mikelle").password("CKZR6V8F70000QO13QJQCNC7X")
                                                 .email("cystal_harlowlm@jon.ozw").fullName("Dove Servin").build();

        UserDocument actual = this.mapper.toUserDocument(command, "cl00xzkzi0000ec13nbsrlfvg");
        UserDocument expected = UserDocument.builder().documentId("cl00xzkzi0000ec13nbsrlfvg")
                                            .id(actual.getId()).password("CKZR6V8F70000QO13QJQCNC7X")
                                            .username("Mikelle").email("cystal_harlowlm@jon.ozw").fullName("Dove Servin").build();

        Assertions.assertThat(this.mapper.toUserDocument(null, null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void toUserDocument_FromCreateUserCommand_IsProperlyMapped() {
        CreateUserCommand command = CreateUserCommand.builder().username("Mikelle").password("CKZR6V8F70000QO13QJQCNC7X")
                                                     .email("cystal_harlowlm@jon.ozw").fullName("Dove Servin").build();
        UserDocument actual = this.mapper.toUserDocument(command);
        UserDocument expected = UserDocument.builder().id(actual.getId())
                                            .username("Mikelle").password("CKZR6V8F70000QO13QJQCNC7X")
                                            .email("cystal_harlowlm@jon.ozw").fullName("Dove Servin").build();

        Assertions.assertThat(this.mapper.toUserDocument((CreateUserCommand) null)).isNull();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    private User buildUser() {
        return User.builder().username("Mikelle").email("cystal_harlowlm@jon.ozw").fullName("Dove Servin").build();
    }
}