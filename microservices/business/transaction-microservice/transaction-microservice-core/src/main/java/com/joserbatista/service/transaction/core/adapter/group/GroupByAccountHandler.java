package com.joserbatista.service.transaction.core.adapter.group;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.adapter.GroupTransactionQueryAdapter;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Account;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GroupByAccountHandler implements GroupTransactionQueryAdapter.GroupByHandler {

    private final GetAccountRemotePort accountRemotePort;

    @Override
    public Group group(GetTransactionQuery query, List<Transaction> transactionList) throws BaseException {
        String username = query.getUsername();
        log.debug("Finding all Accounts for username={}", username);
        List<Account> accountList = this.accountRemotePort.findAllByUsername(username)
                                                          .stream().filter(account -> query.getAccountNameList().contains(account.getName()))
                                                          .toList();

        return Group.builder().by(Group.By.ACCOUNT).startDate(query.getStartDate()).endDate(query.getEndDate())
                    .elementList(this.getElementList(accountList, transactionList)).build();
    }

    private List<Group.Element> getElementList(List<Account> accountList, List<Transaction> transactionList) {
        return accountList.stream()
                          .map(account -> this.toGroupElement(account.getName(), this.getTransactionList(transactionList, account.getId())))
                          .toList();
    }

    private List<Transaction> getTransactionList(List<Transaction> transactionList, String accountId) {
        return transactionList.stream().filter(transaction -> accountId.equals(transaction.getAccountId())).toList();
    }
}
