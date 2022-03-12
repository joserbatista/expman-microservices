package com.joserbatista.service.transaction.outbound.rest.account.adapter;

import com.joserbatista.service.common.validation.exception.OutboundInvocationException;
import com.joserbatista.service.transaction.core.domain.Account;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import com.joserbatista.service.transaction.outbound.rest.account.client.AccountQueryFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GetAccountRestAdapter implements GetAccountRemotePort {

    private AccountQueryFeignClient accountQueryFeignClient;

    @Override
    public boolean existsByUsernameAndId(String username, String accountId) throws OutboundInvocationException {
        return this.accountQueryFeignClient.getAccountById(username, accountId).isPresent();
    }

    @Override
    public List<Account> findAllByUsername(String username) throws OutboundInvocationException {
        return this.accountQueryFeignClient.getAccountList(username);
    }
}
