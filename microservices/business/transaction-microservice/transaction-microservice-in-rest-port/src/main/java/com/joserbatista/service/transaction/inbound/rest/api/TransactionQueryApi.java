package com.joserbatista.service.transaction.inbound.rest.api;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.inbound.rest.dto.GetTransactionQueryDto;
import com.joserbatista.service.transaction.inbound.rest.dto.GroupDto;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(path = "api/user/{username}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "transaction")
@Validated
public interface TransactionQueryApi {

    @GetMapping("/{id}")
    ResponseEntity<TransactionDto> getUserTransactionById(@PathVariable @Username String username, @PathVariable @Uuid String id)
        throws BaseException;

    @GetMapping
    ResponseEntity<List<TransactionDto>> getUserTransactionListByFilter(@PathVariable @Username String username,
        GetTransactionQueryDto queryDto) throws BaseException;

    @GetMapping("group")
    ResponseEntity<GroupDto> groupUserTransactionListByFilter(@PathVariable @Username String username,
        GetTransactionQueryDto queryDto, @RequestParam("by") String groupBy) throws BaseException;
}