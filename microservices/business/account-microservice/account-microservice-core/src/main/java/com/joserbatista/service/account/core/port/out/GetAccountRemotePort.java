package com.joserbatista.service.account.core.port.out;

import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.common.validation.constraint.Username;
import com.joserbatista.service.common.validation.constraint.Uuid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotBlank;

@Validated
public interface GetAccountRemotePort {

    Optional<Account> findByUsernameAndId(@Username String username, @Uuid String id);

    List<Account> findByUsername(@Username String username);

    boolean existsByUserUsernameAndNameIn(@Username String username, @NotBlank String name);

}
