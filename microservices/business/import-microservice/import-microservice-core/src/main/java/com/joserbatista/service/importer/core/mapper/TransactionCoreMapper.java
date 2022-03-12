package com.joserbatista.service.importer.core.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.importer.core.api.command.CreateAccountCommand;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Account;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mapper(config = BaseMapperConfig.class)
public interface TransactionCoreMapper {

    List<CreateTransactionCommand> toCreateTransactionCommandList(List<CreateTransactionCommand> commandList, @Context List<Account> accountList);

    @Mapping(target = "account", source = "account.name", qualifiedByName = "toAccountWithId")
    CreateTransactionCommand toCreateTransactionCommand(CreateTransactionCommand command, @Context List<Account> accountList);

    @Mapping(target = "date", expression = "java(java.time.OffsetDateTime.parse(columnArray[0]))")
    @Mapping(target = "account", expression = "java(Account.builder().name(columnArray[1]).build())")
    @Mapping(target = "tagList", expression = "java(this.toTagList(listSeparator, columnArray))")
    @Mapping(target = "amount", expression = "java(new java.math.BigDecimal(columnArray[3]))")
    @Mapping(target = "note", expression = "java(org.springframework.util.StringUtils.hasText(columnArray[4]) ? columnArray[4] : null)")
    @Mapping(target = "username", source = "username")
    CreateTransactionCommand toCreateTransactionCommand(String username, String listSeparator, String[] columnArray);

    default List<String> toTagList(String listSeparator, String[] columnArray) {
        return Arrays.stream(columnArray[2].split(listSeparator)).filter(Predicate.not(String::isBlank)).distinct().toList();
    }

    @Named("toAccountWithId")
    @Mapping(target = "id", source = ".", qualifiedByName = "toAccountId")
    @Mapping(target = "name", source = ".")
    Account toAccount(String accountName, @Context List<Account> accountList);

    @Named("toAccountId")
    @Nullable
    default String toAccountId(String accountName, @Context List<Account> accountList) throws FailedValidationException {
        return Stream.ofNullable(accountList).flatMap(Collection::stream)
                     .filter(account -> accountName.equalsIgnoreCase(account.getName()))
                     .map(Account::getId).findFirst().orElseThrow(() -> FailedValidationException.builder().build());
    }

    @Nullable
    default List<CreateAccountCommand> toCreateAccountCommandList(String username, List<String> nameList) {
        if (username == null || CollectionUtils.isEmpty(nameList)) {
            return null;
        }
        return nameList.stream().map(accountName -> this.toCreateAccountCommand(username, accountName)).toList();
    }

    @Mapping(target = "name", source = "name")
    @Mapping(target = "username", source = "username")
    CreateAccountCommand toCreateAccountCommand(String username, String name);
}
