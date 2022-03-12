package com.joserbatista.service.user.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.out.UserCommandRemotePort;
import com.joserbatista.service.user.outbound.mongo.document.UserDocument;
import com.joserbatista.service.user.outbound.mongo.mapper.UserMongoMapper;
import com.joserbatista.service.user.outbound.mongo.repository.UserMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@AllArgsConstructor
public class ManageUserMongoAdapter implements UserCommandRemotePort {

    private final UserMongoRepository repository;
    private final UserMongoMapper mapper;

    @Override
    public User create(CreateUserCommand command) {
        log.trace("Handling {}", command);
        UserDocument userDocument = this.mapper.toUserDocument(command);
        UserDocument savedUser = this.repository.insert(userDocument);
        log.trace("User.name={} created with id {}, returning", savedUser.getUsername(), savedUser.getId());
        return this.mapper.toUser(savedUser);
    }

    @Override
    public void removeByUsername(String username) throws ResourceNotFoundException {
        log.trace("Deleting User.username={}", username);
        UserDocument userDocument = this.repository.findByUsername(username)
                                                   .orElseThrow(() -> ResourceNotFoundException.builder().resourceType("User").id(username).build());
        this.repository.deleteById(userDocument.getDocumentId());
    }

    @Override
    public User save(SaveUserCommand command) throws ResourceNotFoundException {
        log.trace("Handling {}", command);
        String username = command.getUsername();
        UserDocument userDocument = this.repository.findByUsername(username)
                                                   .orElseThrow(() -> ResourceNotFoundException.builder().resourceType("User").id(username).build());

        UserDocument userDocumentToSave = this.mapper.toUserDocument(command, userDocument.getDocumentId());
        UserDocument savedUser = this.repository.save(userDocumentToSave);
        log.trace("User.name={} saved, returning", savedUser.getUsername());
        return this.mapper.toUser(savedUser);
    }
}
