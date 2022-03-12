package com.joserbatista.service.transaction.core.port.out;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface TransactionCommandRemotePort {

    Transaction create(@Valid CreateTransactionCommand command) throws BaseException;

    Transaction save(@Valid SaveTransactionCommand command) throws BaseException;

    void remove(@Username String username, String id) throws ResourceNotFoundException;
}
