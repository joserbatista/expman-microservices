package com.joserbatista.service.user.core.port.out;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.user.core.domain.User;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface GetUserRemotePort {

    boolean existsByUsername(@Username String username);

    Optional<User> findByUsername(@Username String username);
}
