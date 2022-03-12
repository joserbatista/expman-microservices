package com.joserbatista.service.account.core.port.in;

import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface GetAccountQueryPort {

    List<AccountType> getAccountTypes();

    Optional<Account> getAccountById(@Username String username, @Uuid String id);

    List<Account> getAccountList(@Username String username);
}
