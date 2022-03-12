package com.joserbatista.service.importer.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.adapter.parser.ImportResourceParser;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.api.command.ImportTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.core.port.in.ImportAccountPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class ImportAccountAdapter implements ImportAccountPort {

    private static final String LOG_DISPATCHING_TO = "Dispatching to {}";
    private final ImportResourceParser resourceProcessor;
    private final ImportFileAccountProcessor accountProcessor;
    private final ImportFileTransactionProcessor transactionProcessor;

    @Override
    public List<Transaction> importFromFile(ImportTransactionCommand command) throws BaseException {
        log.trace("Attempting to import file with {}", command);

        log.trace(LOG_DISPATCHING_TO, this.resourceProcessor.getClass().getSimpleName());
        List<CreateTransactionCommand> createTransactionCommandList = this.resourceProcessor.process(command.getResource(), command.getUsername());

        String username = command.getUsername();
        log.trace("Processing {} Transactions for username={}", createTransactionCommandList.size(), username);

        log.trace(LOG_DISPATCHING_TO, this.accountProcessor.getClass().getSimpleName());
        List<Account> createdAccountList = this.accountProcessor.process(username, createTransactionCommandList);
        log.trace("Created accounts: {}", createdAccountList);

        log.trace(LOG_DISPATCHING_TO, this.transactionProcessor.getClass().getSimpleName());
        return this.transactionProcessor.process(username, createTransactionCommandList);
    }
}