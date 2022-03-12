package com.joserbatista.service.importer.core.port.in;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.importer.core.api.command.ImportTransactionCommand;
import com.joserbatista.service.importer.core.domain.Transaction;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface ImportAccountPort {

    List<Transaction> importFromFile(@NotNull @Valid ImportTransactionCommand command) throws BaseException;
}
