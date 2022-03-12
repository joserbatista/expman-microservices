package com.joserbatista.service.user.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.outbound.mongo.document.UserDocument;
import com.joserbatista.service.user.outbound.mongo.mapper.UserMongoMapper;
import com.joserbatista.service.user.outbound.mongo.repository.UserMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class ManageUserMongoAdapterTest {

    @Mock
    private UserMongoRepository repository;
    @Mock
    private UserMongoMapper mapper;

    @InjectMocks
    private ManageUserMongoAdapter adapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.closeable.close();
    }

    @Test
    void create_ValidParameters_CallsPortMethodOnce() {
        Mockito.when(this.mapper.toUserDocument(ArgumentMatchers.any(CreateUserCommand.class))).thenReturn(this.buildUserDocument());
        Mockito.when(this.repository.insert(ArgumentMatchers.any(UserDocument.class))).thenReturn(this.buildUserDocument());

        this.adapter.create(CreateUserCommand.builder().username("marcheozvf").build());

        Mockito.verify(this.repository).insert(ArgumentMatchers.any(UserDocument.class));
        Mockito.verifyNoMoreInteractions(this.repository);
    }

    @Test
    void remove_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.repository.findByUsername("laquetteba")).thenReturn(Optional.of(UserDocument.builder().build()));

        this.adapter.removeByUsername("laquetteba");

        Mockito.verify(this.repository).findByUsername(ArgumentMatchers.anyString());
        Mockito.verify(this.repository).deleteById(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.repository);
    }

    @Test
    void save_ValidParameters_CallsPortMethodOnce() throws ResourceNotFoundException {
        Mockito.when(this.repository.findByUsername("laquetteba")).thenReturn(Optional.of(UserDocument.builder().build()));
        Mockito.when(this.repository.save(ArgumentMatchers.any())).thenReturn(this.buildUserDocument());

        this.adapter.save(SaveUserCommand.builder().username("laquetteba").build());

        Mockito.verify(this.repository).save(ArgumentMatchers.any());
        Mockito.verify(this.repository).findByUsername(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.repository);
    }

    private UserDocument buildUserDocument() {
        return UserDocument.builder().id("e32d5728-1b63-4d35-bbe8-b64740d139d8").username("marcheozvf").build();
    }

}