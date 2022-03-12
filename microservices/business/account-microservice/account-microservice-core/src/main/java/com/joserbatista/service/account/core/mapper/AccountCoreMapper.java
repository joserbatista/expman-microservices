package com.joserbatista.service.account.core.mapper;

import com.joserbatista.service.account.core.api.command.CreateAccountCommand;
import com.joserbatista.service.common.mapper.BaseMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Mapper(config = BaseMapperConfig.class)
public interface AccountCoreMapper {

    @Nullable
    default List<CreateAccountCommand> toCreateAccountCommandList(String username, Set<String> nameList) {
        if (username == null || CollectionUtils.isEmpty(nameList)) {
            return null;
        }
        return nameList.stream().map(accountName -> this.toCreateAccountCommand(username, accountName)).toList();
    }

    @Mapping(target = "name", source = "name")
    @Mapping(target = "username", source = "username")
    CreateAccountCommand toCreateAccountCommand(String username, String name);
}
