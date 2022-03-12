package com.joserbatista.service.account.core.port.in;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface ManageAccountPort {

    Account create(CreateAccountCommand command) throws BaseException;

    void remove(@Username String username, @Uuid String id) throws BaseException;

    Account save(@Valid @NotNull SaveAccountCommand command) throws BaseException;
}