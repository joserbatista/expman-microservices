package com.joserbatista.service.account.outbound.mongo.adapter;

import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.port.out.GetAccountRemotePort;
import com.joserbatista.service.account.outbound.mongo.document.AccountDocument;
import com.joserbatista.service.account.outbound.mongo.mapper.AccountMongoMapper;
import com.joserbatista.service.account.outbound.mongo.repository.AccountMongoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GetAccountMongoAdapter implements GetAccountRemotePort {

    private final AccountMongoRepository accountMongoRepository;
    private final AccountMongoMapper mapper;

    @Override
    public Optional<Account> findByUsernameAndId(String username, String id) {
        log.debug("Finding Account.id={} for username={}", id, username);
        return this.accountMongoRepository.findByUsernameAndId(username, id).map(this.mapper::toAccount);
    }

    @Override
    public List<Account> findByUsername(String username) {
        log.debug("Finding all Accounts for username={}", username);
        List<AccountDocument> accountDocumentList = this.accountMongoRepository.findAllByUsername(username);
        return this.mapper.toAccountList(accountDocumentList);
    }

    @Override
    public boolean existsByUserUsernameAndNameIn(String username, String name) {
        log.debug("Finding if Account.name={} exists for username={}", name, username);
        return this.accountMongoRepository.existsByUsernameAndName(username, name);
    }
}
