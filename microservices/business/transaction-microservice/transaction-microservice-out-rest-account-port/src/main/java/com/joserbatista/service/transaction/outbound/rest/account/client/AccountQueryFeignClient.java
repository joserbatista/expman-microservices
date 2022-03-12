package com.joserbatista.service.transaction.outbound.rest.account.client;

import com.joserbatista.service.common.validation.exception.OutboundInvocationException;
import com.joserbatista.service.transaction.core.domain.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient("account-microservice")
public interface AccountQueryFeignClient {

    @GetMapping("/api/user/{username}/account/{id}")
    Optional<Account> getAccountById(@PathVariable String username, @PathVariable String id) throws OutboundInvocationException;

    @GetMapping("/api/user/{username}/account")
    List<Account> getAccountList(@PathVariable String username) throws OutboundInvocationException;
}