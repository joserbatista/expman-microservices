package com.joserbatista.service.account.outbound.mongo.adapter;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.port.out.AccountCommandRemotePort;
import com.joserbatista.service.account.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.account.outbound.mongo.mapper.AccountMongoMapper;
import com.joserbatista.service.account.outbound.mongo.repository.AccountMongoRepository;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.DuplicatedResourceException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class ManageAccountMongoAdapter implements AccountCommandRemotePort {

    private final AccountMongoMapper mapper;
    private final AccountMongoRepository accountMongoRepository;

    @Override
    public Account create(CreateAccountCommand command) throws BaseException {
        log.trace("Handling {}", command);
        this.validateAccountDuplicate(command.getUsername(), command.getName());
        return this.createAccount(command.getUsername(), Set.of(command)).get(0);
    }

    @Override
    public List<Account> createList(String username, List<CreateAccountCommand> commandList) throws BaseException {
        log.trace("Handling {} CreateAccountCommands", commandList.size());
        for (CreateAccountCommand account : commandList) {
            this.validateAccountDuplicate(username, account.getName());
        }
        return this.createAccount(username, commandList);
    }

    private List<Account> createAccount(String username, Collection<CreateAccountCommand> commandList) {
        List<AccountDocument> accountDocumentList = this.mapper.toAccountDocumentList(commandList, username);

        log.trace("Creating {} accounts", commandList.size());
        Iterable<AccountDocument> savedAccountDocumentIterable = this.accountMongoRepository.saveAll(accountDocumentList);
        List<Account> savedAccountList = this.mapper.toAccountList(savedAccountDocumentIterable);
        log.trace("Created {} accounts, returning", savedAccountList.size());

        return savedAccountList;
    }

    @Override
    public Account save(SaveAccountCommand command) throws BaseException {
        log.trace("Handling {}", command);
        String username = command.getUsername();
        AccountDocument existingDocument = this.findByUsernameAndId(username, command.getId());

        AccountDocument accountDocument = this.mapper.toAccountDocument(command, existingDocument.getDocumentId());

        log.trace("Saving Account.name={}", accountDocument.getName());
        AccountDocument savedAccountDocument = this.accountMongoRepository.save(accountDocument);

        log.trace("Account.name={} saved, returning", accountDocument.getName());
        return this.mapper.toAccount(savedAccountDocument);
    }

    @Override
    public void remove(String username, String id) throws ResourceNotFoundException {
        log.trace("Deleting Account.id={}", id);
        AccountDocument accountDocument = this.findByUsernameAndId(username, id);
        this.accountMongoRepository.deleteById(accountDocument.getDocumentId());
    }

    private AccountDocument findByUsernameAndId(String username, String id) throws ResourceNotFoundException {
        log.trace("Validating if Account.id={} for User.username={} exists", id, username);
        return this.accountMongoRepository.findByUsernameAndId(username, id)
                                          .orElseThrow(() -> ResourceNotFoundException.builder().resourceType("Account").id(id).build());
    }

    private void validateAccountDuplicate(String username, String accountName) throws DuplicatedResourceException {
        log.trace("Validating if Account.name={} for User.username={} exists", accountName, username);
        boolean existsByUsernameAndName = this.accountMongoRepository.existsByUsernameAndName(username, accountName);
        if (existsByUsernameAndName) {
            log.trace("Account.name={} already exists for User.username={}, failing", accountName, username);
            throw DuplicatedResourceException.builder().resourceType("Account").id(accountName).build();
        }
    }
}
