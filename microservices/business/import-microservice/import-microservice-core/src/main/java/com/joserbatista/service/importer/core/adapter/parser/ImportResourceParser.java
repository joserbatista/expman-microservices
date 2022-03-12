package com.joserbatista.service.importer.core.adapter.parser;

import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.business.FailedValidationException;
import com.joserbatista.service.importer.core.api.command.CreateTransactionCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ImportResourceParser {

    private final List<Parser> parserList;

    public List<CreateTransactionCommand> process(Resource resource, String username) throws BaseException {
        String extension = FilenameUtils.getExtension(resource.getFilename());
        Parser parser = this.createByExtension(extension);

        log.trace("Dispatching resource <{}> to {}", resource.getFilename(), parser.getClass().getSimpleName());
        return parser.parse(username, resource);
    }

    Parser createByExtension(String extension) throws FailedValidationException {
        String errorDescription = "Importing <%s> files is not supported".formatted(extension);
        return this.parserList.stream().filter(parser -> parser.isExtensionSupported(extension)).findFirst()
                              .orElseThrow(() -> FailedValidationException.builder().errorDescription(errorDescription).build());
    }

    interface Parser {

        boolean isExtensionSupported(String extension);

        List<CreateTransactionCommand> parse(String username, Resource resource) throws BaseException;
    }
}
