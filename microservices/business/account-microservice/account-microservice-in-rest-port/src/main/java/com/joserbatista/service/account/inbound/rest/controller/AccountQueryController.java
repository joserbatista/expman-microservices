package com.joserbatista.service.account.inbound.rest.controller;

import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.account.core.port.in.GetAccountQueryPort;
import com.joserbatista.service.account.inbound.rest.api.AccountQueryApi;
import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.account.inbound.rest.dto.AccountTypeEnumDto;
import com.joserbatista.service.account.inbound.rest.mapper.AccountRestMapper;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class AccountQueryController implements AccountQueryApi {

    private final GetAccountQueryPort getAccountQueryPort;
    private final AccountRestMapper mapper;

    @Override
    public ResponseEntity<List<AccountTypeEnumDto>> getAccountTypes(String username) {
        List<AccountType> accountTypeList = this.getAccountQueryPort.getAccountTypes();
        List<AccountTypeEnumDto> accountTypeEnumDtoList = this.mapper.toAccountTypeEnumDtoList(accountTypeList);
        return ResponseEntity.ok(accountTypeEnumDtoList);
    }

    @Override
    public ResponseEntity<AccountDto> getAccountById(String username, String id) throws BaseException {
        Account account = this.getAccount(username, id);

        AccountDto accountDto = this.mapper.toAccountDto(account);
        return ResponseEntity.ok(accountDto);
    }

    @Override
    public ResponseEntity<List<AccountDto>> getAccountList(String username) {
        List<Account> accountList = this.getAccountQueryPort.getAccountList(username);
        List<AccountDto> accountDtoList = this.mapper.toAccountDtoList(accountList);
        return ResponseEntity.ok(accountDtoList);
    }

    private Account getAccount(String username, String id) throws BaseException {
        return this.getAccountQueryPort.getAccountById(username, id)
                                       .orElseThrow(() -> ResourceNotFoundException.builder().resourceType("Account").id(id).build());
    }
}