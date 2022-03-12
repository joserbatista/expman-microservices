package com.joserbatista.service.transaction.inbound.rest.controller;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.in.ManageTransactionPort;
import com.joserbatista.service.transaction.inbound.rest.api.TransactionCommandApi;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
import com.joserbatista.service.transaction.inbound.rest.mapper.TransactionRestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class TransactionCommandController implements TransactionCommandApi {

    private final TransactionRestMapper mapper;
    private final ManageTransactionPort transactionPort;

    @Override
    public ResponseEntity<TransactionDto> createUserTransaction(String username, TransactionDto transactionDto) throws BaseException {
        CreateTransactionCommand transaction = this.mapper.toCreateTransactionCommand(username, transactionDto);
        Transaction saved = this.transactionPort.create(transaction);

        TransactionDto createdTransactionDto = this.mapper.toTransactionDto(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransactionDto);
    }

    @Override
    public ResponseEntity<TransactionDto> updateUserTransaction(String username, TransactionDto transactionDto, String id) throws BaseException {
        SaveTransactionCommand transaction = this.mapper.toSaveTransactionCommand(username, id, transactionDto);
        Transaction saved = this.transactionPort.save(transaction);

        TransactionDto updatedTransactionDto = this.mapper.toTransactionDto(saved);

        return ResponseEntity.ok(updatedTransactionDto);
    }

    @Override
    public ResponseEntity<Void> removeUserTransaction(String username, String id) throws BaseException {
        this.transactionPort.remove(username, id);

        return ResponseEntity.noContent().build();
    }
}