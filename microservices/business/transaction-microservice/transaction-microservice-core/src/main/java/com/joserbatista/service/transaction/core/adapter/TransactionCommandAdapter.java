package com.joserbatista.service.transaction.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.in.ManageTransactionPort;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import com.joserbatista.service.transaction.core.port.out.TransactionCommandRemotePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Adapter that implements the required steps to answer {@link ManageTransactionPort}
 */
@Service
@Slf4j
@AllArgsConstructor
public class TransactionCommandAdapter implements ManageTransactionPort {

    private final TransactionCommandRemotePort commandRemotePort;
    private final GetAccountRemotePort getAccountRemotePort;

    @Override
    public Transaction create(CreateTransactionCommand command) throws BaseException {
        if (this.isAccountIdInvalid(command.getUsername(), command.getAccountId())) {
            throw FailedValidationException.builder().errorMessage("Account <%s> not found".formatted(command.getAccountId())).build();
        }

        log.trace("Dispatching {} to {}", command, this.commandRemotePort.getClass().getSimpleName());
        return this.commandRemotePort.create(command);
    }

    @Override
    public void remove(String username, String id) throws BaseException {
        log.trace("Dispatching remove Transaction.id={} to {}", id, this.commandRemotePort.getClass().getSimpleName());
        this.commandRemotePort.remove(username, id);
    }

    @Override
    public Transaction save(SaveTransactionCommand command) throws BaseException {
        if (this.isAccountIdInvalid(command.getUsername(), command.getAccountId())) {
            throw FailedValidationException.builder().errorMessage("Account <%s> not found".formatted(command.getAccountId())).build();
        }

        log.trace("Dispatching {} to {}", command, this.commandRemotePort.getClass().getSimpleName());
        return this.commandRemotePort.save(command);
    }

    private boolean isAccountIdInvalid(String username, String accountId) throws BaseException {
        log.trace("Validating if Account.Id={} exists for User.username={}", username, accountId);
        if (accountId == null || accountId.isEmpty()) {
            log.trace("<accountId> is missing, failing");
            return true;
        }

        return !this.getAccountRemotePort.existsByUsernameAndId(username, accountId);
    }

}
