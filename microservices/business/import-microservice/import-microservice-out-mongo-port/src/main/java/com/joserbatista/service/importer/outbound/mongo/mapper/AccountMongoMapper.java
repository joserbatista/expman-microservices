package com.joserbatista.service.importer.outbound.mongo.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.importer.core.api.command.CreateAccountCommand;
import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface AccountMongoMapper {

    List<Account> toAccountList(Iterable<AccountDocument> accountDocumentList);

    Account toAccount(AccountDocument accountDocument);

    default List<AccountDocument> toAccountDocumentList(Collection<CreateAccountCommand> accountList, String userId) {
        if (CollectionUtils.isEmpty(accountList)) {
            return List.of();
        }
        return accountList.stream().map(account -> this.toAccountDocument(account, userId)).toList();
    }

    @Mapping(target = "active", constant = "true")
    AccountDocument toAccountDocument(CreateAccountCommand accountCommand, String username);

}
