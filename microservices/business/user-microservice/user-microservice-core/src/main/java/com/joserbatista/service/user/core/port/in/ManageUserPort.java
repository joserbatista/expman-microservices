package com.joserbatista.service.user.core.port.in;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface ManageUserPort {

    User create(@Valid @NotNull CreateUserCommand command) throws BaseException;

    User save(@Valid @NotNull SaveUserCommand command) throws BaseException;

    void removeByUsername(@Username String username) throws BaseException;
}
