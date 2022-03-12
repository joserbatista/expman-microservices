package com.joserbatista.service.importer.core.adapter.parser;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.common.validation.exception.internal.GenericInternalErrorException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import com.joserbatista.service.importer.core.mapper.TransactionCoreMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ImportCsvParser implements ImportResourceParser.Parser {

    private static final String SEPARATOR = ";";
    private static final String LIST_SEPARATOR = "\\|";
    private static final String EXPECTED_HEADER = "date%saccount%stags%samount%snote".replace("%s", SEPARATOR);
    private static final int SPLIT_LIMIT = StringUtils.countOccurrencesOf(EXPECTED_HEADER, SEPARATOR) + 1;

    private final TransactionCoreMapper transactionCoreMapper;

    @Override
    public boolean isExtensionSupported(String extension) {
        return "csv".equals(extension);
    }

    @Override
    public List<CreateTransactionCommand> parse(String username, Resource resource) throws BaseException {
        log.debug("Handling resource <{}> for {}", resource.getFilename(), username);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();

            log.trace("Validating header {}", header);
            if (!EXPECTED_HEADER.equals(header)) {
                log.debug("Invalid header, failing");
                throw FailedValidationException.builder().errorDescription("Invalid header for import").build();
            }

            log.trace("Creating CreateTransactionCommands");
            List<CreateTransactionCommand> transactionList = reader.lines().map(line -> this.toTransaction(username, line)).toList();
            log.trace("Mapped {} Transaction instances", transactionList.size());
            return transactionList;
        } catch (IOException e) {
            log.warn("IOException caught, wrapping: {}", e.getMessage());
            throw GenericInternalErrorException.builder().cause(e).intention("Reading file").build();
        }
    }

    private CreateTransactionCommand toTransaction(String username, String line) {
        String[] columnArray = line.split(SEPARATOR, SPLIT_LIMIT);
        return this.transactionCoreMapper.toCreateTransactionCommand(username, LIST_SEPARATOR, columnArray);
    }

}
