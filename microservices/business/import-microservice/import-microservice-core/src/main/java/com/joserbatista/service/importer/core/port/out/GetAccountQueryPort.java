package com.joserbatista.service.importer.core.port.out;

import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.exception.OutboundInvocationException;
import com.joserbatista.service.importer.core.domain.Account;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface GetAccountQueryPort {

    List<Account> findAllByUsername(@Username String username) throws OutboundInvocationException;
}
