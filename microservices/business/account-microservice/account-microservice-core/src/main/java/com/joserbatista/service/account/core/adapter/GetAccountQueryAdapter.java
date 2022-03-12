package com.joserbatista.service.account.core.adapter;

import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.account.core.port.in.GetAccountQueryPort;
import com.joserbatista.service.account.core.port.out.GetAccountRemotePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the required steps to answer {@link GetAccountQueryPort}
 */
@Service
@Slf4j
@AllArgsConstructor
public class GetAccountQueryAdapter implements GetAccountQueryPort {

    private final GetAccountRemotePort accountPort;

    @Override
    public List<AccountType> getAccountTypes() {
        log.debug("Finding AccountTypes");
        return Arrays.asList(AccountType.values());
    }

    @Override
    public Optional<Account> getAccountById(String username, String id) {
        log.debug("Finding Account.id={} for username={} using {}", id, username, this.accountPort.getClass().getSimpleName());
        return this.accountPort.findByUsernameAndId(username, id);
    }

    @Override
    public List<Account> getAccountList(String username) {
        log.debug("Finding all Accounts for username={} using {}", username, this.accountPort.getClass().getSimpleName());
        return this.accountPort.findByUsername(username);
    }

}
