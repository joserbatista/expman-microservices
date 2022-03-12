package com.joserbatista.service.user.inbound.rest.controller;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.user.core.api.command.CreateUserCommand;
import com.joserbatista.service.user.core.api.command.SaveUserCommand;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.in.ManageUserPort;
import com.joserbatista.service.user.inbound.rest.api.UserCommandApi;
import com.joserbatista.service.user.inbound.rest.dto.UserDto;
import com.joserbatista.service.user.inbound.rest.mapper.UserRestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class UserCommandController implements UserCommandApi {

    private final UserRestMapper mapper;
    private final ManageUserPort manageUserPort;

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) throws BaseException {
        CreateUserCommand command = this.mapper.toCreateUserCommand(userDto);
        User saved = this.manageUserPort.create(command);

        UserDto createdUserDto = this.mapper.toUserDto(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(String username, UserDto userDto) throws BaseException {
        SaveUserCommand command = this.mapper.toSaveUserCommand(username, userDto);
        User saved = this.manageUserPort.save(command);

        UserDto createdUserDto = this.mapper.toUserDto(saved);

        return ResponseEntity.ok(createdUserDto);
    }

    @Override
    public ResponseEntity<Void> removeUser(String username) throws BaseException {
        this.manageUserPort.removeByUsername(username);

        return ResponseEntity.noContent().build();
    }

}