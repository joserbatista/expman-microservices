package com.joserbatista.service.transaction.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.in.GetTransactionQueryPort;
import com.joserbatista.service.transaction.core.port.out.GetTransactionRemotePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the required steps to answer {@link GetTransactionQueryPort}
 */
@Service
@Slf4j
@AllArgsConstructor
public class GetTransactionQueryAdapter implements GetTransactionQueryPort {

    private final GetTransactionRemotePort transactionPort;

    @Override
    public List<Transaction> getByFilter(GetTransactionQuery getTransactionQuery) throws BaseException {
        log.debug("Finding Transactions for query={} using {}", getTransactionQuery, this.transactionPort.getClass().getSimpleName());
        return this.transactionPort.findByQuery(getTransactionQuery);
    }

    @Override
    public Optional<Transaction> getUserTransactionById(String username, String id) {
        log.debug("Finding Transaction.id={} for username={} using {}", id, username, this.transactionPort.getClass().getSimpleName());
        return this.transactionPort.findByUsernameAndId(username, id);
    }

}
