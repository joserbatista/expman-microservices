package com.joserbatista.service.transaction.inbound.rest.api;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
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

@RequestMapping(path = "api/user/{username}/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "transaction")
@Validated
public interface TransactionCommandApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<TransactionDto> createUserTransaction(@PathVariable @Username String username,
        @RequestBody TransactionDto transactionDto)
        throws BaseException;

    @PutMapping("/{id}")
    ResponseEntity<TransactionDto> updateUserTransaction(@PathVariable @Username String username, @RequestBody TransactionDto transactionDto,
        @PathVariable @Uuid String id) throws BaseException;

    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> removeUserTransaction(@PathVariable @Username String username, @PathVariable @Uuid String id)
        throws BaseException;
}