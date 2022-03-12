package com.joserbatista.service.importer.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.DuplicatedResourceException;
import com.joserbatista.service.importer.core.api.command.CreateAccountCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.port.out.CreateAccountPort;
import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.importer.outbound.mongo.mapper.AccountMongoMapper;
import com.joserbatista.service.importer.outbound.mongo.repository.AccountMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ManageAccountMongoAdapter implements CreateAccountPort {

    private final AccountMongoMapper mapper;
    private final AccountMongoRepository accountMongoRepository;

    @Override
    public List<Account> createList(String username, List<CreateAccountCommand> commandList) throws BaseException {
        log.trace("Handling {} CreateAccountCommands", commandList.size());
        for (CreateAccountCommand account : commandList) {
            this.validateAccountDuplicate(username, account.getName());
        }

        List<AccountDocument> accountDocumentList = this.mapper.toAccountDocumentList(commandList, username);

        log.trace("Creating {} accounts", commandList.size());
        Iterable<AccountDocument> savedAccountDocumentIterable = this.accountMongoRepository.saveAll(accountDocumentList);
        List<Account> savedAccountList = this.mapper.toAccountList(savedAccountDocumentIterable);
        log.trace("Created {} accounts, returning", savedAccountList.size());

        return savedAccountList;
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
