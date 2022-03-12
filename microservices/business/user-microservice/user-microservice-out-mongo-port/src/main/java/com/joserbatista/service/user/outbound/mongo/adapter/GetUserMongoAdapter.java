package com.joserbatista.service.user.outbound.mongo.adapter;

import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.out.GetUserRemotePort;
import com.joserbatista.service.user.outbound.mongo.mapper.UserMongoMapper;
import com.joserbatista.service.user.outbound.mongo.repository.UserMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GetUserMongoAdapter implements GetUserRemotePort {

    private final UserMongoRepository userMongoRepository;
    private final UserMongoMapper mapper;

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Finding if User.username={} exists", username);
        return this.userMongoRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Finding User.username={}", username);
        return this.userMongoRepository.findByUsername(username).map(this.mapper::toUser);
    }
}
