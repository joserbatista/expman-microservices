package com.joserbatista.service.user.core.adapter;

import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.in.GetUserQueryPort;
import com.joserbatista.service.user.core.port.out.GetUserRemotePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Adapter that implements the required steps to answer {@link GetUserQueryPort}
 */
@Service
@Slf4j
@AllArgsConstructor
public class GetUserQueryAdapter implements GetUserQueryPort {

    private final GetUserRemotePort getUserRemotePort;

    @Override
    public Optional<User> getByUsername(String username) {
        log.debug("Finding User.username={} using {}", username, this.getUserRemotePort.getClass().getSimpleName());
        return this.getUserRemotePort.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Finding if User.username={} exists using {}", username, this.getUserRemotePort.getClass().getSimpleName());
        return this.getUserRemotePort.existsByUsername(username);
    }

}
