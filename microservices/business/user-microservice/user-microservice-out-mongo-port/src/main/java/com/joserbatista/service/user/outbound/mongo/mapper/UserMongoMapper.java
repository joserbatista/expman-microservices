package com.joserbatista.service.user.outbound.mongo.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.outbound.mongo.document.UserDocument;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface UserMongoMapper {

    User toUser(UserDocument userDocument);

    UserDocument toUserDocument(SaveUserCommand command, final String documentId);

    UserDocument toUserDocument(CreateUserCommand command);
}
