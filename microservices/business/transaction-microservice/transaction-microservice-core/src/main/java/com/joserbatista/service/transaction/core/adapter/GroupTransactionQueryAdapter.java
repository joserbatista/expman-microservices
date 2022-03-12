package com.joserbatista.service.transaction.core.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.api.query.GroupTransactionQueryPort;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.in.GetTransactionQueryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Adapter that implements the required steps to answer {@link GroupTransactionQueryPort}
 */
@Service
@Slf4j
@AllArgsConstructor
public class GroupTransactionQueryAdapter implements GroupTransactionQueryPort {

    private final GetTransactionQueryPort queryPort;
    private final Map<String, GroupByHandler> groupByHandlerList;

    @Override
    public Group byFilter(GetTransactionQuery getTransactionQuery, Group.By groupBy) throws BaseException {
        log.debug("Finding Transactions for query={} using {}", getTransactionQuery, this.queryPort.getClass().getSimpleName());
        List<Transaction> transactionList = this.queryPort.getByFilter(getTransactionQuery);

        GroupByHandler handler = this.getTransactionGroup(groupBy);
        log.trace("Dispatching to {}", handler.getClass().getSimpleName());
        Group group = handler.group(getTransactionQuery, transactionList);

        log.trace("Grouping done, returning <{}> elements", group.getElementList().size());
        return group;
    }

    private GroupByHandler getTransactionGroup(Group.By groupBy) throws FailedValidationException {
        if (groupBy == null) {
            throw FailedValidationException.builder().build();
        }

        return switch (groupBy) {
            case TAG -> this.groupByHandlerList.get("groupByTagHandler");
            case ACCOUNT -> this.groupByHandlerList.get("groupByAccountHandler");
        };
    }

    public interface GroupByHandler {

        Group group(GetTransactionQuery query, List<Transaction> transactionList) throws BaseException;

        default Group.Element toGroupElement(String value, List<Transaction> transactionList) {
            return Group.Element.builder()
                                .value(value).transactionCount(transactionList.size())
                                .idList(transactionList.stream().map(Transaction::getId).toList())
                                .totalAmount(transactionList.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                                .build();
        }
    }
}
