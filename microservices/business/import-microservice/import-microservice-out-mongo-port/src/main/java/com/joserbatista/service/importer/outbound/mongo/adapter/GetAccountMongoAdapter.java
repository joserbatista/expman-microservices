package com.joserbatista.service.importer.outbound.mongo.adapter;

import com.joserbatista.service.importer.core.domain.Account;
import com.joserbatista.service.importer.core.port.out.GetAccountQueryPort;
import com.joserbatista.service.importer.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.importer.outbound.mongo.mapper.AccountMongoMapper;
import com.joserbatista.service.importer.outbound.mongo.repository.AccountMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GetAccountMongoAdapter implements GetAccountQueryPort {

    private final AccountMongoRepository accountMongoRepository;
    private final AccountMongoMapper mapper;

    @Override
    public List<Account> findAllByUsername(String username) {
        log.debug("Finding all Accounts for username={}", username);
        List<AccountDocument> accountDocumentList = this.accountMongoRepository.findAllByUsername(username);
        return this.mapper.toAccountList(accountDocumentList);
    }

}
