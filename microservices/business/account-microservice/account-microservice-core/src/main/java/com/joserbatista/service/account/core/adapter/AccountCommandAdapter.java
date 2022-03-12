package com.joserbatista.service.account.core.adapter;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.port.in.ManageAccountPort;
import com.joserbatista.service.account.core.port.out.AccountCommandRemotePort;
import com.joserbatista.service.common.validation.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Adapter that implements the required steps to answer {@link ManageAccountPort}
 */
@Service
@Slf4j
public class AccountCommandAdapter implements ManageAccountPort {

    private final AccountCommandRemotePort commandRemotePort;

    @Autowired
    public AccountCommandAdapter(AccountCommandRemotePort commandRemotePort) {
        this.commandRemotePort = commandRemotePort;
    }

    @Override
    public Account create(CreateAccountCommand command) throws BaseException {
        log.trace("Dispatching {} to {}", command, this.commandRemotePort.getClass().getSimpleName());
        return this.commandRemotePort.create(command);
    }

    @Override
    public void remove(String username, String id) throws BaseException {
        log.trace("Dispatching remove Account.id={} to {}", id, this.commandRemotePort.getClass().getSimpleName());
        this.commandRemotePort.remove(username, id);
    }

    @Override
    public Account save(SaveAccountCommand command) throws BaseException {
        log.trace("Dispatching {} to {}", command, this.commandRemotePort.getClass().getSimpleName());
        return this.commandRemotePort.save(command);
    }

}
