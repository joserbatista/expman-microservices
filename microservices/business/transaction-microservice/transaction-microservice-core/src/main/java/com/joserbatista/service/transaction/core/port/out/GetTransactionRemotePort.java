package com.joserbatista.service.transaction.core.port.out;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Transaction;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface GetTransactionRemotePort {

    Optional<Transaction> findByUsernameAndId(@Username String username, @Uuid String id);

    List<Transaction> findByQuery(@NotNull @Valid GetTransactionQuery getTransactionQuery) throws BaseException;
}
