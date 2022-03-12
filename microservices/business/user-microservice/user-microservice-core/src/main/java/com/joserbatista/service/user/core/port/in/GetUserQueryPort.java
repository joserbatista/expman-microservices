package com.joserbatista.service.user.core.port.in;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.user.core.domain.User;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface GetUserQueryPort {

    Optional<User> getByUsername(@Username String username);

    boolean existsByUsername(@Username String username);
}
