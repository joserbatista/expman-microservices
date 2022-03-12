package com.joserbatista.service.account.core.port.out;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Validated
public interface AccountCommandRemotePort {

    Account create(@NotNull @Valid CreateAccountCommand command) throws BaseException;

    Account save(@NotNull @Valid SaveAccountCommand command) throws BaseException;

    void remove(@Username String username, @Uuid String id) throws ResourceNotFoundException;

    List<Account> createList(@Username String username, @NotEmpty List<CreateAccountCommand> commandList) throws BaseException;
}