package com.joserbatista.service.importer.core.port.out;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateAccountCommand;
import com.joserbatista.service.importer.core.domain.Account;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotEmpty;

@Validated
public interface CreateAccountPort {

    List<Account> createList(@Username String username, @NotEmpty List<CreateAccountCommand> commandList) throws BaseException;
}