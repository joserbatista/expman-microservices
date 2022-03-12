package com.joserbatista.service.transaction.outbound.rest.account.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joserbatista.service.common.validation.exception.outbound.OutboundSystemBadRequestException;
import com.joserbatista.service.common.validation.exception.outbound.OutboundSystemUnexpectedErrorException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.io.IOException;

@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("Response is not successful: {}", response.status());

        HttpStatus responseStatus = HttpStatus.resolve(response.status());
        Response.Body errorBody = response.body();

        String requestUrl = response.request().url();
        if (responseStatus == null || errorBody == null) {
            return OutboundSystemBadRequestException.builder().system(requestUrl).build();
        }

        String errorDescription = readErrorBody(errorBody);
        if (responseStatus.is4xxClientError()) {
            return OutboundSystemBadRequestException.builder()
                                                    .errorDescription(errorDescription)
                                                    .system(requestUrl)
                                                    .systemStatusCode(String.valueOf(responseStatus.value()))
                                                    .build();
        }

        return OutboundSystemUnexpectedErrorException.builder()
                                                     .errorDescription(errorDescription)
                                                     .system(requestUrl)
                                                     .systemStatusCode(String.valueOf(responseStatus.value()))
                                                     .build();

    }

    @Nullable
    private static String readErrorBody(Response.Body errorBody) {
        if (errorBody == null) {
            return null;
        }
        try {
            ErrorTemplate errorTemplate = new ObjectMapper().readValue(errorBody.asInputStream(), ErrorTemplate.class);
            log.debug("Wrapped error response <{}>", errorTemplate);
            return "%s: %s [%s]".formatted(errorTemplate.getErrorCode(), errorTemplate.getErrorMessage(), errorTemplate.getErrorDescription());
        } catch (IOException e) {
            log.warn("Error deserializing ErrorTemplate from response", e);
            return null;
        }
    }
}
