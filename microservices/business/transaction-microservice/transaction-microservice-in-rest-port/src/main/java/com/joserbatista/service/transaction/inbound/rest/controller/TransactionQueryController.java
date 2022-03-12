package com.joserbatista.service.transaction.inbound.rest.controller;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.api.query.GroupTransactionQueryPort;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.in.GetTransactionQueryPort;
import com.joserbatista.service.transaction.inbound.rest.api.TransactionQueryApi;
import com.joserbatista.service.transaction.inbound.rest.dto.GetTransactionQueryDto;
import com.joserbatista.service.transaction.inbound.rest.dto.GroupDto;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
import com.joserbatista.service.transaction.inbound.rest.mapper.TransactionRestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class TransactionQueryController implements TransactionQueryApi {

    private final GetTransactionQueryPort queryPort;
    private final GroupTransactionQueryPort groupPort;
    private final TransactionRestMapper mapper;

    @Override
    public ResponseEntity<TransactionDto> getUserTransactionById(String username, String id) throws BaseException {
        Transaction user = this.queryPort.getUserTransactionById(username, id)
                                         .orElseThrow(() -> ResourceNotFoundException.builder().resourceType("Transaction").id(id).build());
        TransactionDto userDto = this.mapper.toTransactionDto(user);

        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<List<TransactionDto>> getUserTransactionListByFilter(String username, GetTransactionQueryDto queryDto)
        throws BaseException {
        if (queryDto == null || !queryDto.hasValues()) {
            throw FailedValidationException.builder().errorDescription("Filter must not be null").build();
        }

        GetTransactionQuery transactionFilter = this.mapper.toGetTransactionQuery(queryDto, username);

        List<Transaction> transactionList = this.queryPort.getByFilter(transactionFilter);
        List<TransactionDto> transactionDtoList = this.mapper.toTransactionDtoList(transactionList);

        return ResponseEntity.ok(transactionDtoList);
    }

    @Override
    public ResponseEntity<GroupDto> groupUserTransactionListByFilter(String username, GetTransactionQueryDto queryDto, String groupBy)
        throws BaseException {
        if (queryDto == null || !queryDto.hasValues()) {
            throw FailedValidationException.builder().errorDescription("Filter must not be null").build();
        }

        GetTransactionQuery transactionFilter = this.mapper.toGetTransactionQuery(queryDto, username);

        if (Arrays.stream(Group.By.values()).map(Enum::name).noneMatch(groupBy::equals)) {
            throw FailedValidationException.builder().errorDescription("Invalid groupBy type <%s>".formatted(groupBy)).build();
        }

        Group group = this.groupPort.byFilter(transactionFilter, Group.By.valueOf(groupBy));
        GroupDto groupDto = this.mapper.toTransactionGroupDto(group);

        return ResponseEntity.ok(groupDto);
    }
}