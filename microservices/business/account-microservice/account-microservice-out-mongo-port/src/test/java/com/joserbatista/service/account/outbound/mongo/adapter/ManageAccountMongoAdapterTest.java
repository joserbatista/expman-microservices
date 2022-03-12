package com.joserbatista.service.account.outbound.mongo.adapter;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.account.outbound.mongo.mapper.AccountMongoMapper;
import com.joserbatista.service.account.outbound.mongo.repository.AccountMongoRepository;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.DuplicatedResourceException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class ManageAccountMongoAdapterTest {

    @Mock
    private AccountMongoMapper mapper;
    @Mock
    private AccountMongoRepository accountMongoRepository;

    @InjectMocks
    private ManageAccountMongoAdapter adapter;

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
    void createList_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.accountMongoRepository.existsByUsernameAndName("laquetteba", "Credit Account")).thenReturn(false);
        Mockito.when(this.mapper.toAccountList(ArgumentMatchers.any())).thenReturn(List.of(this.buildAccount()));

        this.adapter.createList("laquetteba", List.of(CreateAccountCommand.builder().username("laquetteba").name("Credit Account").build()));

        Mockito.verify(this.accountMongoRepository).existsByUsernameAndName(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.accountMongoRepository).saveAll(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    @Test
    void create_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.accountMongoRepository.existsByUsernameAndName("laquetteba", "Credit Account")).thenReturn(false);
        Mockito.when(this.mapper.toAccountList(ArgumentMatchers.any())).thenReturn(List.of(this.buildAccount()));

        this.adapter.create(CreateAccountCommand.builder().username("laquetteba").name("Credit Account").build());

        Mockito.verify(this.accountMongoRepository).existsByUsernameAndName(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.accountMongoRepository).saveAll(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    @Test
    void create_DuplicatedResource_ThrowsDuplicatedResourceException() {
        Mockito.when(this.accountMongoRepository.existsByUsernameAndName("laquetteba", "Credit Account")).thenReturn(true);

        Assertions.assertThatExceptionOfType(DuplicatedResourceException.class)
                  .isThrownBy(() -> this.adapter.create(CreateAccountCommand.builder().username("laquetteba").name("Credit Account").build()));

        Mockito.verify(this.accountMongoRepository).existsByUsernameAndName(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    @Test
    void remove_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.accountMongoRepository.findByUsernameAndId("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63"))
               .thenReturn(Optional.ofNullable(buildAccountDocument()));

        this.adapter.remove("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63");

        Mockito.verify(this.accountMongoRepository).deleteById(ArgumentMatchers.anyString());
        Mockito.verify(this.accountMongoRepository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    @Test
    void remove_ResourceNotFound_ThrowsResourceNotFoundException() {
        Mockito.when(this.accountMongoRepository.findByUsernameAndId("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63"))
               .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> this.adapter.remove("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63"));

        Mockito.verify(this.accountMongoRepository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    @Test
    void save_ValidParameters_CallsPortMethodOnce() throws BaseException {
        Mockito.when(this.accountMongoRepository.findByUsernameAndId("laquetteba", "9c30aa46-924a-410b-8a83-d160e4122640"))
               .thenReturn(Optional.ofNullable(buildAccountDocument()));
        Mockito.when(this.mapper.toAccountDocument(ArgumentMatchers.any(SaveAccountCommand.class), ArgumentMatchers.any()))
               .thenReturn(buildAccountDocument());

        this.adapter.save(this.buildSaveAccountCommand());

        Mockito.verify(this.accountMongoRepository).findByUsernameAndId(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(this.accountMongoRepository).save(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    @Test
    void save_ResourceNotFound_ThrowsResourceNotFoundException() {
        Mockito.when(this.accountMongoRepository.findByUsernameAndId("laquetteba", "5cc0e481-d819-49e7-8ee1-347dc08e9e63"))
               .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> this.adapter.save(this.buildSaveAccountCommand()));

        Mockito.verify(this.accountMongoRepository).findByUsernameAndId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.accountMongoRepository);
    }

    //<editor-fold desc="builders">
    private SaveAccountCommand buildSaveAccountCommand() {
        return SaveAccountCommand.builder().id("9c30aa46-924a-410b-8a83-d160e4122640").name("Credit Account").username("laquetteba").build();
    }

    private Account buildAccount() {
        return Account.builder().id("9c30aa46-924a-410b-8a83-d160e4122640").name("Credit Account").build();
    }

    private AccountDocument buildAccountDocument() {
        return AccountDocument.builder()
                              .documentId("cl0122z810001ec13qrz692pp")
                              .id("9c30aa46-924a-410b-8a83-d160e4122640")
                              .name("Credit Account")
                              .build();
    }
    //</editor-fold>

}