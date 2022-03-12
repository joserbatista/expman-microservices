package com.joserbatista.service.importer.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateAccountCommand;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.mapper.TransactionCoreMapper;
import com.joserbatista.service.importer.core.port.out.CreateAccountPort;
import com.joserbatista.service.importer.core.port.out.GetAccountQueryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Component
@AllArgsConstructor
class ImportFileAccountProcessor {

    private final CreateAccountPort accountPort;
    private final GetAccountQueryPort queryPort;
    private final TransactionCoreMapper transactionCoreMapper;

    List<Account> process(String username, List<CreateTransactionCommand> createTransactionCommandList) throws BaseException {
        log.trace("Creating missing Accounts for {}", username);

        log.trace("Fetching existing Accounts for {}", username);
        List<String> existingAccountNameList = this.queryPort.findAllByUsername(username).stream().map(Account::getName).toList();

        log.trace("Found {} Accounts for {}", existingAccountNameList.size(), username);
        List<String> accountToCreateNameList = createTransactionCommandList.stream().map(CreateTransactionCommand::getAccount)
                                                                           .map(Account::getName).distinct()
                                                                           .filter(Predicate.not(existingAccountNameList::contains))
                                                                           .toList();

        if (accountToCreateNameList.isEmpty()) {
            log.trace("No accounts to create, proceeding");
            return List.of();
        }

        log.trace("New accounts to create: {}", accountToCreateNameList);
        List<CreateAccountCommand> accountsToCreateList = this.transactionCoreMapper.toCreateAccountCommandList(username, accountToCreateNameList);
        return this.accountPort.createList(username, accountsToCreateList);
    }

}
