package com.joserbatista.service.transaction.inbound.rest.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.api.query.GetTransactionQuery;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.inbound.rest.dto.GetTransactionQueryDto;
import com.joserbatista.service.transaction.inbound.rest.dto.GroupDto;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface TransactionRestMapper {

    @Mapping(target = "id", source = "id")
    SaveTransactionCommand toSaveTransactionCommand(String username, String id, TransactionDto transactionDto);

    CreateTransactionCommand toCreateTransactionCommand(String username, TransactionDto transactionDto);

    TransactionDto toTransactionDto(Transaction transaction);

    @Mapping(target = "tagValueList", source = "queryDto.tagList")
    GetTransactionQuery toGetTransactionQuery(GetTransactionQueryDto queryDto, String username);

    List<TransactionDto> toTransactionDtoList(List<Transaction> transactionList);

    GroupDto toTransactionGroupDto(Group group);
}
