package com.joserbatista.service.transaction.core.port.in;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface ManageTransactionPort {

    Transaction create(@Valid @NotNull CreateTransactionCommand command) throws BaseException;

    void remove(@Username String username, @Uuid String id) throws BaseException;

    Transaction save(@Valid @NotNull SaveTransactionCommand command) throws BaseException;
}
