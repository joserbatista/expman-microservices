package com.joserbatista.service.transaction.inbound.rest.controller;

import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import com.joserbatista.service.transaction.core.api.query.GroupTransactionQueryPort;
import com.joserbatista.service.transaction.core.domain.Group;
import com.joserbatista.service.transaction.core.domain.Transaction;
import com.joserbatista.service.transaction.core.port.in.GetTransactionQueryPort;
import com.joserbatista.service.transaction.inbound.rest.dto.TransactionDto;
import com.joserbatista.service.transaction.inbound.rest.mapper.TransactionRestMapper;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Test class for {@link TransactionQueryController}
 */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = TransactionQueryController.class)
@ContextConfiguration(classes = TransactionQueryController.class)
@Import(GlobalExceptionHandler.class)
class TransactionQueryControllerTest {

    private static final String URL_TEMPLATE = "/api/user/candancerr0/transaction/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetTransactionQueryPort getTransactionQueryPort;
    @MockBean
    private GroupTransactionQueryPort groupTransactionQueryPort;
    @MockBean
    private TransactionRestMapper mapper;

    @Test
    void getUserTransactionById_ValidParameters_ReturnsCode200() throws Exception {
        Mockito.when(this.getTransactionQueryPort.getUserTransactionById(ArgumentMatchers.any(), ArgumentMatchers.any()))
               .thenReturn(Optional.of(this.buildTransaction()));
        Mockito.when(this.mapper.toTransactionDto(ArgumentMatchers.any()))
               .thenReturn(TransactionDto.builder().amount(BigDecimal.valueOf(2.92)).date(OffsetDateTime.parse("2007-12-03T10:15:30Z"))
                                         .note("Locked footage hood duties antarctica bald justin, note.")
                                         .tagList(Set.of("Syria", "SolarRoad", "Annada")).build());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/{accountId}", "b9ed0648-677b-493a-9291-0c21591ab26d"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getUserTransactionById_NotFound_ReturnsCode404() throws Exception {
        Mockito.when(this.getTransactionQueryPort.getUserTransactionById(ArgumentMatchers.any(), ArgumentMatchers.any()))
               .thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/{accountId}", "b9ed0648-677b-493a-9291-0c21591ab26d"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getUserTransactionListByFilter_ValidParameters_ReturnsCode200() throws Exception {
        Mockito.when(this.getTransactionQueryPort.getByFilter(ArgumentMatchers.any()))
               .thenReturn(List.of(this.buildTransaction()));
        Mockito.when(this.mapper.toTransactionDtoList(ArgumentMatchers.any()))
               .thenReturn(List.of(TransactionDto.builder().amount(BigDecimal.valueOf(2.92)).date(OffsetDateTime.parse("2007-12-03T10:15:30Z"))
                                                 .note("Locked footage hood duties antarctica bald justin, note.")
                                                 .tagList(Set.of("Syria", "SolarRoad", "Annada")).build()));

        String filterUrlTemplate = URL_TEMPLATE + "?startDate={startDate}&endDate={endDate}&accountNameList={accountNameList}&tagList={tagList}";
        this.mockMvc.perform(MockMvcRequestBuilders.get(filterUrlTemplate, "2007-12-03T00:00:30Z", "2007-12-03T23:59:30Z",
                                                        "Credit Card Account,Checking Account", "Tag1,Tag2"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getUserTransactionListByFilter_InvalidFilter_ReturnsCode400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "?startDate={startDate}", ""))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("ERR-400"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Validation failed"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").value("Filter must not be null"));
    }

    @Test
    void groupUserTransactionListByFilter_InvalidFilter_ReturnsCode400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "group?by={by}&startDate={startDate}", "TAG", ""))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("ERR-400"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Validation failed"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").value("Filter must not be null"));
    }

    @Test
    void groupUserTransactionListByFilter_InvalidBy_ReturnsCode400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "group?by={by}&startDate={startDate}",
                                                        "INVALID", "2007-12-03T00:00:30Z"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("ERR-400"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Validation failed"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").value("Invalid groupBy type <INVALID>"));
    }

    @Test
    void groupUserTransactionListByFilter_ValidParameters_ReturnsCode200() throws Exception {
        Mockito.when(this.groupTransactionQueryPort.byFilter(ArgumentMatchers.any(), ArgumentMatchers.any()))
               .thenReturn(Group.builder().build());
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "group?by={by}&startDate={startDate}", "TAG", "2007-12-03T00:00:30Z"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Transaction buildTransaction() {
        return Transaction.builder().amount(BigDecimal.valueOf(2.92))
                          .date(OffsetDateTime.parse("2007-12-03T10:15:30Z"))
                          .note("Locked footage hood duties antarctica bald justin, note.")
                          .tagList(Set.of("Syria", "SolarRoad", "Annada")).build();
    }

}