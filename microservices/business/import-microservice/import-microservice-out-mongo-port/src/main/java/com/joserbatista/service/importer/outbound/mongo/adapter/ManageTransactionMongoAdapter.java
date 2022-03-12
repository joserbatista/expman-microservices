package com.joserbatista.service.importer.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.core.port.out.CreateTransactionPort;
import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.importer.outbound.mongo.document.TransactionDocument;
import com.joserbatista.service.importer.outbound.mongo.mapper.TransactionMongoMapper;
import com.joserbatista.service.importer.outbound.mongo.repository.AccountMongoRepository;
import com.joserbatista.service.importer.outbound.mongo.repository.TransactionMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@Slf4j
@AllArgsConstructor
public class ManageTransactionMongoAdapter implements CreateTransactionPort {

    private final TransactionMongoMapper mapper;
    private final TransactionMongoRepository transactionMongoRepository;
    private final AccountMongoRepository accountMongoRepository;

    @Override
    public List<Transaction> createList(String username, List<CreateTransactionCommand> commandList) throws BaseException {
        log.trace("Handling {} CreateTransactionCommands", commandList.size());
        return this.createTransactionList(username, commandList);
    }

    private List<Transaction> createTransactionList(String username, List<CreateTransactionCommand> commandList) throws BaseException {
        List<Account> accountList = commandList.stream().map(CreateTransactionCommand::getAccount).filter(Objects::nonNull).distinct().toList();
        log.trace("Validating AccountList.size={}", accountList.size());
        if (this.containsInvalidAccountId(username, accountList)) {
            log.trace("An invalid Account was found, failing");
            throw FailedValidationException.builder().errorDescription("Invalid AccountId").build();
        }

        List<TransactionDocument> transactionDocumentList = this.mapper.toTransactionDocumentList(commandList);
        log.trace("Creating {} Transactions", commandList.size());
        Iterable<TransactionDocument> savedTransactionDocumentList = this.transactionMongoRepository.insert(transactionDocumentList);
        List<Transaction> transactionList = this.mapper.toTransactionList(savedTransactionDocumentList);
        log.trace("Created {} Transactions, returning", transactionList.size());
        return transactionList;
    }

    private boolean containsInvalidAccountId(String username, List<Account> accountList) {
        boolean anyAccountIdMissing = accountList.stream().anyMatch(account -> account == null || !StringUtils.hasLength(account.getId()));
        if (accountList.isEmpty() || anyAccountIdMissing) {
            log.trace("An accountId is missing, failing");
            return true;
        }

        List<String> accountIdList = this.accountMongoRepository.findAllByUsername(username).stream().map(AccountDocument::getId).filter(
            Predicate.not(String::isBlank)).toList();

        List<String> missingAccountNameList = accountList.stream()
                                                         .filter(Predicate.not(account -> accountIdList.contains(account.getId())))
                                                         .map(Account::getName).toList();

        log.trace("The following accounts are missing: {}", missingAccountNameList);
        return !missingAccountNameList.isEmpty();
    }

}
