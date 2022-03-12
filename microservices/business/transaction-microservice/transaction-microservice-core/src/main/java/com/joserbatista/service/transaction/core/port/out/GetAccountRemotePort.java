package com.joserbatista.service.transaction.core.port.out;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.transaction.core.domain.Account;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface GetAccountRemotePort {

    boolean existsByUsernameAndId(String username, String accountId) throws BaseException;

    List<Account> findAllByUsername(String username) throws BaseException;
}
