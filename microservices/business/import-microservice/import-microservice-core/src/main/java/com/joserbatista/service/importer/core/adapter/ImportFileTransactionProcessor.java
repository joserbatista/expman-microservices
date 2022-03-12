package com.joserbatista.service.importer.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.core.mapper.TransactionCoreMapper;
import com.joserbatista.service.importer.core.port.out.CreateTransactionPort;
import com.joserbatista.service.importer.core.port.out.GetAccountQueryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
class ImportFileTransactionProcessor {

    private final GetAccountQueryPort queryPort;
    private final TransactionCoreMapper mapper;
    private final CreateTransactionPort transactionPort;

    List<Transaction> process(String username, List<CreateTransactionCommand> sourceCommandList) throws BaseException {
        List<Account> accountList = this.queryPort.findAllByUsername(username);
        log.trace("Adding Account.id to Transactions");

        List<CreateTransactionCommand> createTransactionCommandList = this.mapper.toCreateTransactionCommandList(sourceCommandList, accountList);

        List<Transaction> createdTransactionList = this.transactionPort.createList(username, createTransactionCommandList);
        log.debug("All done, returning {} Transactions", createdTransactionList.size());
        return createdTransactionList;
    }

}
