package com.joserbatista.service.user.core.port.out;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface UserCommandRemotePort {

    User create(@NotNull @Valid CreateUserCommand command);

    User save(@NotNull @Valid SaveUserCommand command) throws ResourceNotFoundException;

    void removeByUsername(@Username String username) throws BaseException;
}