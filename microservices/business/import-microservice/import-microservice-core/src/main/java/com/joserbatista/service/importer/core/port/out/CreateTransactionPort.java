package com.joserbatista.service.importer.core.port.out;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Transaction;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Validated
public interface CreateTransactionPort {

    List<Transaction> createList(@Username String username, @NotEmpty @Valid List<CreateTransactionCommand> commandList) throws BaseException;
}
