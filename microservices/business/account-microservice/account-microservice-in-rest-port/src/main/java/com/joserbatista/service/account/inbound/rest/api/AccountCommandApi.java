package com.joserbatista.service.account.inbound.rest.api;

import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(path = "api/user/{username}/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "account")
@Validated
public interface AccountCommandApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<AccountDto> createAccount(@Username @PathVariable String username, @RequestBody AccountDto accountDto)
        throws BaseException;

    @PutMapping("/{id}")
    ResponseEntity<AccountDto> updateAccount(@PathVariable @Username String username, @RequestBody AccountDto accountDto,
        @PathVariable @Uuid String id) throws BaseException;

    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> removeAccount(@PathVariable @Username String username, @PathVariable @Uuid String id) throws BaseException;
}
