package com.joserbatista.service.importer.inbound.rest.controller;

import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import com.joserbatista.service.importer.core.port.in.ImportAccountPort;
import com.joserbatista.service.importer.inbound.rest.mapper.ImportRestMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ImportCommandController.class)
@ContextConfiguration(classes = ImportCommandController.class)
@Import(GlobalExceptionHandler.class)
class ImportCommandControllerTest {

    private static final String USERNAME = "kaylynnee4v";
    private static final String URL_TEMPLATE = "/api/user/" + USERNAME + "/transaction";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportRestMapper mapper;

    @MockBean
    private ImportAccountPort importAccountPort;

    @Test
    void importUserTransactions_ValidRequest_ReturnsCode201() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE + "/import")
                                                   .file("file", "data".getBytes(StandardCharsets.UTF_8)))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(this.importAccountPort).importFromFile(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.importAccountPort);
    }

}