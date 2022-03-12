package com.joserbatista.service.account.inbound.rest.controller;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.port.in.ManageAccountPort;
import com.joserbatista.service.account.inbound.rest.api.AccountCommandApi;
import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.account.inbound.rest.mapper.AccountRestMapper;
import com.joserbatista.service.common.validation.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class AccountCommandController implements AccountCommandApi {

    private final AccountRestMapper mapper;
    private final ManageAccountPort manageAccountPort;

    @Override
    public ResponseEntity<AccountDto> createAccount(String username, AccountDto accountDto) throws BaseException {
        CreateAccountCommand command = this.mapper.toCreateAccountCommand(username, accountDto);

        Account saved = this.manageAccountPort.create(command);
        AccountDto createdAccountDto = this.mapper.toAccountDto(saved);
        log.trace("{}'s Account created with id {}", username, createdAccountDto.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccountDto);
    }

    @Override
    public ResponseEntity<AccountDto> updateAccount(String username, AccountDto accountDto, String id) throws BaseException {
        SaveAccountCommand command = this.mapper.toSaveAccountCommand(username, id, accountDto);

        Account saved = this.manageAccountPort.save(command);
        AccountDto createdAccountDto = this.mapper.toAccountDto(saved);
        log.trace("{}'s Account updated with id {}", username, createdAccountDto.getId());

        return ResponseEntity.ok(createdAccountDto);
    }

    @Override
    public ResponseEntity<Void> removeAccount(String username, String id) throws BaseException {
        this.manageAccountPort.remove(username, id);
        log.trace("{}'s Account removed with id {}", username, id);

        return ResponseEntity.noContent().build();
    }

}