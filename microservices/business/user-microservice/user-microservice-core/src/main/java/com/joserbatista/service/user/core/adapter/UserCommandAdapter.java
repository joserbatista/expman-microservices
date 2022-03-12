package com.joserbatista.service.user.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.DuplicatedResourceException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.in.GetUserQueryPort;
import com.joserbatista.service.user.core.port.in.ManageUserPort;
import com.joserbatista.service.user.core.port.out.UserCommandRemotePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Adapter that implements the required steps to answer {@link ManageUserPort}
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserCommandAdapter implements ManageUserPort {

    private final UserCommandRemotePort commandRemotePort;
    private final GetUserQueryPort queryPort;

    @Override
    public User create(CreateUserCommand command) throws BaseException {
        String username = command.getUsername();

        log.trace("Validating if User.username={} exists", username);
        boolean existsByUsername = this.queryPort.existsByUsername(username);
        if (existsByUsername) {
            log.debug("User.username={} already exists, failing", username);
            throw DuplicatedResourceException.builder().resourceType("User").id(username).build();
        }

        log.trace("Dispatching {} to {}", command, this.commandRemotePort.getClass().getSimpleName());
        return this.commandRemotePort.create(command);
    }

    @Override
    public void removeByUsername(String username) throws BaseException {
        this.validateUserExistence(username);
        log.trace("Dispatching remove User.username={} to {}", username, this.commandRemotePort.getClass().getSimpleName());
        this.commandRemotePort.removeByUsername(username);
    }

    @Override
    public User save(SaveUserCommand command) throws BaseException {
        String username = command.getUsername();
        this.validateUserExistence(username);
        log.trace("Dispatching {} to {}", command, this.commandRemotePort.getClass().getSimpleName());
        return this.commandRemotePort.save(command);
    }

    private void validateUserExistence(String username) throws ResourceNotFoundException {
        log.trace("Validating if User.username={} exists", username);
        boolean isUserNonExistent = !this.queryPort.existsByUsername(username);
        if (isUserNonExistent) {
            log.debug("User.username={} does not exist, failing", username);
            throw ResourceNotFoundException.builder().resourceType("User").id(username).build();
        }
    }

}