package com.joserbatista.service.account.inbound.rest.controller;

import com.joserbatista.service.account.core.domain.Account;
import com.joserbatista.service.account.core.port.in.GetAccountQueryPort;
import com.joserbatista.service.account.inbound.rest.dto.AccountDto;
import com.joserbatista.service.account.inbound.rest.dto.AccountTypeEnumDto;
import com.joserbatista.service.account.inbound.rest.mapper.AccountRestMapper;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
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

import java.util.List;
import java.util.Optional;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AccountQueryController.class)
@ContextConfiguration(classes = AccountQueryController.class)
@Import(GlobalExceptionHandler.class)
class AccountQueryControllerTest {

    private static final String URL_TEMPLATE = "/api/user/rianna18/account/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAccountQueryPort getAccountQueryPort;
    @MockBean
    private AccountRestMapper mapper;

    @Test
    void getAccountTypes_ValidParameters_ReturnsCode200() throws Exception {
        Mockito.when(this.mapper.toAccountTypeEnumDtoList(ArgumentMatchers.any()))
               .thenReturn(List.of(AccountTypeEnumDto.values()));

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "types"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAccountById_ValidParameters_ReturnsCode200() throws Exception {
        Mockito.when(this.getAccountQueryPort.getAccountById(ArgumentMatchers.any(), ArgumentMatchers.any()))
               .thenReturn(Optional.ofNullable(Account.builder().id("b9ed0648-677b-493a-9291-0c21591ab26d").build()));
        Mockito.when(this.mapper.toAccountDto(ArgumentMatchers.any()))
               .thenReturn(AccountDto.builder().id("b9ed0648-677b-493a-9291-0c21591ab26d").build());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "{accountId}", "b9ed0648-677b-493a-9291-0c21591ab26d"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAccountById_NotFound_ReturnsCode404() throws Exception {
        Mockito.when(this.getAccountQueryPort.getAccountById(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "{accountId}", "4636b395-939b-4e79-947c-c0168ba667a4"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getCurrentUserEntityList_ValidParameters_ReturnsCode200() throws Exception {
        List<Account> accountList = List.of(
            Account.builder().id("b9ed0648-677b-493a-9291-0c21591ab26d").build(), Account.builder().id("3c4f73bb-7024-452e-990d-b9543cf4ca72").build()
        );
        Mockito.when(this.getAccountQueryPort.getAccountList(ArgumentMatchers.any())).thenReturn(accountList);
        Mockito.when(this.mapper.toAccountDto(ArgumentMatchers.any()))
               .thenReturn(AccountDto.builder().id("b9ed0648-677b-493a-9291-0c21591ab26d").build());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

}