package com.joserbatista.service.account.inbound.rest.api;

import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.account.inbound.rest.dto.AccountTypeEnumDto;
import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "api/user/{username}/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "account")
@Validated
public interface AccountQueryApi {

    @GetMapping("/types")
    ResponseEntity<List<AccountTypeEnumDto>> getAccountTypes(@PathVariable(required = false) @Username String username);

    @GetMapping("/{id}")
    ResponseEntity<AccountDto> getAccountById(@PathVariable @Username String username, @PathVariable @Uuid String id)
        throws BaseException;

    @GetMapping
    ResponseEntity<List<AccountDto>> getAccountList(@PathVariable @Username String username) throws BaseException;
}