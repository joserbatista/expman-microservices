package com.joserbatista.service.transaction.core.adapter.group;

import com.joserbatista.service.transaction.core.adapter.GroupTransactionQueryAdapter;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GroupByTagHandler implements GroupTransactionQueryAdapter.GroupByHandler {

    @Override
    public Group group(GetTransactionQuery query, List<Transaction> transactionList) {
        return Group.builder().by(Group.By.TAG).startDate(query.getStartDate()).endDate(query.getEndDate())
                    .elementList(this.getElementList(query.getTagValueList(), transactionList)).build();
    }

    private List<Group.Element> getElementList(Collection<String> tagValueList, List<Transaction> transactionList) {
        return Stream.ofNullable(tagValueList).flatMap(Collection::stream)
                     .map(tagValue -> this.toGroupElement(tagValue, this.getTransactionList(transactionList, tagValue))).toList();
    }

    private List<Transaction> getTransactionList(List<Transaction> transactionList, String tagValue) {
        return Stream.ofNullable(transactionList).flatMap(Collection::stream)
                     .filter(transaction -> transaction.getTagList().contains(tagValue)).toList();
    }
}
