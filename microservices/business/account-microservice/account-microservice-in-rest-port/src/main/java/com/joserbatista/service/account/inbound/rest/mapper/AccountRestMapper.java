package com.joserbatista.service.account.inbound.rest.mapper;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.account.core.api.command.SaveAccountCommand;
import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.domain.AccountType;
import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.account.inbound.rest.dto.AccountTypeEnumDto;
import com.joserbatista.service.common.mapper.BaseMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface AccountRestMapper {

    List<AccountTypeEnumDto> toAccountTypeEnumDtoList(List<AccountType> accountTypeList);

    List<AccountDto> toAccountDtoList(List<Account> accountList);

    AccountDto toAccountDto(Account account);

    @Mapping(target = "id", source = "id")
    SaveAccountCommand toSaveAccountCommand(String username, String id, AccountDto accountDto);

    CreateAccountCommand toCreateAccountCommand(String username, AccountDto accountDto);
}
