package com.joserbatista.service.user.inbound.rest.api;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.user.inbound.rest.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "api/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "user")
@Validated
public interface UserQueryApi {

    @GetMapping
    ResponseEntity<UserDto> getUserByUsername(@PathVariable @Username String username) throws BaseException;

}