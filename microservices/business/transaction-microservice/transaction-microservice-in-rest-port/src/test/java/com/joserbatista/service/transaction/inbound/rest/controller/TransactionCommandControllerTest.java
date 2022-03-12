package com.joserbatista.service.transaction.inbound.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import com.joserbatista.service.transaction.core.port.in.ManageTransactionPort;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
import com.joserbatista.service.transaction.inbound.rest.mapper.TransactionRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Set;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = TransactionCommandController.class)
@ContextConfiguration(classes = TransactionCommandController.class)
@Import(GlobalExceptionHandler.class)
class TransactionCommandControllerTest {

    private static final String USERNAME = "kaylynnee4v";
    private static final String URL_TEMPLATE = "/api/user/" + USERNAME + "/transaction";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRestMapper mapper;
    @MockBean
    private ManageTransactionPort transactionPort;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = this.mapperBuilder.build();
    }

    @Test
    void createUserTransaction_ValidRequest_ReturnsCode201() throws Exception {
        TransactionDto transactionDto = this.buildTransactionDto();

        Mockito.when(this.mapper.toTransactionDto(ArgumentMatchers.any())).thenReturn(transactionDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                                                   .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                                   .content(this.objectMapper.writeValueAsBytes(transactionDto)))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(this.transactionPort).create(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.transactionPort);
    }

    @Test
    void updateUserTransaction_ValidRequest_ReturnsCode200() throws Exception {
        TransactionDto transactionDto = this.buildTransactionDto();

        Mockito.when(this.mapper.toTransactionDto(ArgumentMatchers.any())).thenReturn(transactionDto);

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/{transactionId}", "a9e07005-975f-4e80-8645-b5e83d102c3e")
                                                   .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                                   .content(this.objectMapper.writeValueAsBytes(transactionDto)))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(this.transactionPort).save(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.transactionPort);
    }

    @Test
    void removeUserTransaction_ValidRequest_ReturnsCode204() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/{transactionId}", "a9e07005-975f-4e80-8645-b5e83d102c3e"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.transactionPort).remove(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.transactionPort);
    }

    private TransactionDto buildTransactionDto() {
        return TransactionDto.builder().amount(BigDecimal.valueOf(2.92))
                             .date(OffsetDateTime.parse("2022-12-03T10:15:30Z")).note("Locked footage hood duties antarctica bald justin, note.")
                             .accountId("774e9e31-cd61-4155-b132-00342b865f3f")
                             .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();
    }
}