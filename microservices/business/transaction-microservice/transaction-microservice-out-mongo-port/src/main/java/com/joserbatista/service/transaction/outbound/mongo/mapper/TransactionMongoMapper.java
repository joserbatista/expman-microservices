package com.joserbatista.service.transaction.outbound.mongo.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.outbound.mongo.document.TransactionDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface TransactionMongoMapper {

    List<TransactionDocument> toTransactionDocumentList(List<CreateTransactionCommand> commandList);

    List<Transaction> toTransactionList(Iterable<TransactionDocument> transactionDocument);

    Transaction toTransaction(TransactionDocument transactionDocument);

    TransactionDocument toTransactionDocument(SaveTransactionCommand saveTransactionCommand, final String documentId);

    TransactionDocument toTransactionDocument(CreateTransactionCommand createTransactionCommand);
}
