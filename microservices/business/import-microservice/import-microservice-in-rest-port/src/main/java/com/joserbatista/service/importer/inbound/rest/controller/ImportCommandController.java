package com.joserbatista.service.importer.inbound.rest.controller;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.ImportTransactionCommand;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.core.port.in.ImportAccountPort;
import com.joserbatista.service.importer.inbound.rest.api.ImportCommandApi;
import com.joserbatista.service.importer.inbound.rest.dto.TransactionDto;
import com.joserbatista.service.importer.inbound.rest.mapper.ImportRestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class ImportCommandController implements ImportCommandApi {

    private final ImportRestMapper mapper;
    private final ImportAccountPort importAccountPort;

    @Override
    public ResponseEntity<List<TransactionDto>> importUserTransactions(String username, MultipartFile file) throws BaseException {
        ImportTransactionCommand command = this.mapper.toImportTransactionCommand(username, file);
        List<Transaction> transactionList = this.importAccountPort.importFromFile(command);
        List<TransactionDto> transactionDtoList = this.mapper.toTransactionDtoList(transactionList);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDtoList);
    }
}