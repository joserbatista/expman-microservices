package com.joserbatista.service.importer.inbound.rest.mapper;

import com.joserbatista.service.common.mapper.BaseMapperConfig;
import com.joserbatista.service.importer.core.api.command.ImportTransactionCommand;
import com.joserbatista.service.importer.core.domain.Transaction;
import com.joserbatista.service.importer.inbound.rest.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface ImportRestMapper {

    List<TransactionDto> toTransactionDtoList(List<Transaction> transactionList);

    ImportTransactionCommand toImportTransactionCommand(String username, MultipartFile file);
}
