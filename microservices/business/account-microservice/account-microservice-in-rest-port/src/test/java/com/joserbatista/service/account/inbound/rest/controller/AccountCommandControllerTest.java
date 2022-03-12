package com.joserbatista.service.account.inbound.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joserbatista.service.account.core.port.in.ManageAccountPort;
import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.account.inbound.rest.dto.AccountTypeEnumDto;
import com.joserbatista.service.account.inbound.rest.mapper.AccountRestMapper;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
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

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AccountCommandController.class)
@ContextConfiguration(classes = AccountCommandController.class)
@Import(GlobalExceptionHandler.class)
class AccountCommandControllerTest {

    private static final String URL_TEMPLATE = "/api/user/robynjmp/account/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRestMapper mapper;
    @MockBean
    private ManageAccountPort port;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = this.mapperBuilder.build();
    }

    @Test
    void createAccount_ValidRequest_ReturnsCode201() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                                          .name("BCP").type(AccountTypeEnumDto.BANK).active(true).amount(BigDecimal.valueOf(256L))
                                          .notes("Millenium BCP")
                                          .build();

        Mockito.when(this.mapper.toAccountDto(ArgumentMatchers.any())).thenReturn(
            AccountDto.builder().id("03ecccf4-4189-4b57-9175-86f1904ce1de").build());

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                                                   .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                                   .content(this.objectMapper.writeValueAsBytes(accountDto)))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("03ecccf4-4189-4b57-9175-86f1904ce1de"));

        Mockito.verify(this.port).create(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void updateAccount_ValidRequest_ReturnsCode200() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                                          .name("BCP").type(AccountTypeEnumDto.BANK).active(true).amount(BigDecimal.valueOf(256L))
                                          .notes("Millenium BCP")
                                          .build();

        Mockito.when(this.mapper.toAccountDto(ArgumentMatchers.any())).thenReturn(
            AccountDto.builder().id("03ecccf4-4189-4b57-9175-86f1904ce1de").build());

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "{id}", "03ecccf4-4189-4b57-9175-86f1904ce1de")
                                                   .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                                   .content(this.objectMapper.writeValueAsBytes(accountDto)))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("03ecccf4-4189-4b57-9175-86f1904ce1de"));

        Mockito.verify(this.port).save(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void removeAccount_ValidRequest_ReturnsCode204() throws Exception {
        Mockito.when(this.mapper.toAccountDto(ArgumentMatchers.any())).thenReturn(
            AccountDto.builder().id("03ecccf4-4189-4b57-9175-86f1904ce1de").build());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "{id}", "03ecccf4-4189-4b57-9175-86f1904ce1de"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.port).remove(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }
}