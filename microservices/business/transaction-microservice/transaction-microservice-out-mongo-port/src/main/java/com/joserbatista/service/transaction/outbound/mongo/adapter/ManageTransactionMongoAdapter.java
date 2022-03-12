package com.joserbatista.service.transaction.outbound.mongo.adapter;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.ResourceNotFoundException;
import com.joserbatista.service.transaction.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.transaction.core.api.command.SaveTransactionCommand;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.out.TransactionCommandRemotePort;
import com.joserbatista.service.transaction.outbound.mongo.document.TransactionDocument;
import com.joserbatista.service.transaction.outbound.mongo.mapper.TransactionMongoMapper;
import com.joserbatista.service.transaction.outbound.mongo.repository.TransactionMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ManageTransactionMongoAdapter implements TransactionCommandRemotePort {

    private final TransactionMongoMapper mapper;
    private final TransactionMongoRepository transactionMongoRepository;

    @Override
    public Transaction create(CreateTransactionCommand command) throws BaseException {
        log.trace("Saving Transaction");
        TransactionDocument transactionDocument = this.mapper.toTransactionDocument(command);
        TransactionDocument savedTransactionDocument = this.transactionMongoRepository.insert(transactionDocument);
        log.trace("Transaction {} saved", savedTransactionDocument.getId());

        return this.mapper.toTransaction(savedTransactionDocument);
    }

    @Override
    public Transaction save(SaveTransactionCommand command) throws BaseException {
        String username = command.getUsername();
        TransactionDocument existingDocument = this.findByUsernameAndId(username, command.getId());
        log.trace("Saving {} Transaction", command.getId());
        TransactionDocument transactionDocument = this.mapper.toTransactionDocument(command, existingDocument.getDocumentId());
        TransactionDocument savedTransactionDocument = this.transactionMongoRepository.save(transactionDocument);
        log.trace("Transaction {} saved", command.getId());

        return this.mapper.toTransaction(savedTransactionDocument);
    }

    @Override
    public void remove(String username, String id) throws ResourceNotFoundException {
        log.trace("Deleting Transaction.id={}", id);
        TransactionDocument document = this.findByUsernameAndId(username, id);
        this.transactionMongoRepository.deleteById(document.getDocumentId());
    }

    private TransactionDocument findByUsernameAndId(String username, String id) throws ResourceNotFoundException {
        log.trace("Validating if Transaction.id={} for User.username={} exists", id, username);
        return this.transactionMongoRepository.findByUsernameAndId(username, id)
                                              .orElseThrow(() -> ResourceNotFoundException.builder().resourceType("Transaction").id(id).build());
    }
}
