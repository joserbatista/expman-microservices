package com.joserbatista.service.user.inbound.rest.controller;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.in.GetUserQueryPort;
import com.joserbatista.service.user.inbound.rest.api.UserQueryApi;
import com.joserbatista.service.user.inbound.rest.dto.UserDto;
import com.joserbatista.service.user.inbound.rest.mapper.UserRestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class UserQueryController implements UserQueryApi {

    private final GetUserQueryPort getUserQueryPort;
    private final UserRestMapper mapper;

    @Override
    public ResponseEntity<UserDto> getUserByUsername(String username) throws BaseException {
        User user = this.getUserQueryPort.getByUsername(username)
                                         .orElseThrow(() -> ResourceNotFoundException.builder().resourceType("User").id(username).build());
        UserDto userDto = this.mapper.toUserDto(user);

        return ResponseEntity.ok(userDto);
    }

}