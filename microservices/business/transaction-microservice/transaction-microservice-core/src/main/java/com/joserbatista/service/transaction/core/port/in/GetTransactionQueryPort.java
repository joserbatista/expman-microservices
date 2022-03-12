package com.joserbatista.service.transaction.core.port.in;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Transaction;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@Validated
public interface GetTransactionQueryPort {

    List<Transaction> getByFilter(@Valid GetTransactionQuery getTransactionQuery) throws BaseException;

    Optional<Transaction> getUserTransactionById(@Username String username, @Valid String id);
}
