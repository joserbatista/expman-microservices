package com.joserbatista.service.importer.outbound.mongo.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.outbound.mongo.document.TransactionDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface TransactionMongoMapper {

    List<TransactionDocument> toTransactionDocumentList(List<CreateTransactionCommand> commandList);

    List<Transaction> toTransactionList(Iterable<TransactionDocument> transactionDocument);

    Transaction toTransaction(TransactionDocument transactionDocument);

    @Mapping(target = "accountId", source = "account.id")
    TransactionDocument toTransactionDocument(CreateTransactionCommand createTransactionCommand);
}
