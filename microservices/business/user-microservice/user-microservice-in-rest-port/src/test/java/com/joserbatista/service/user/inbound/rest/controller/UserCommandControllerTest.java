package com.joserbatista.service.user.inbound.rest.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import com.joserbatista.service.user.core.port.in.ManageUserPort;
import com.joserbatista.service.user.inbound.rest.dto.UserDto;
import com.joserbatista.service.user.inbound.rest.mapper.UserRestMapper;
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

import java.nio.charset.StandardCharsets;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserCommandController.class)
@ContextConfiguration(classes = UserCommandController.class)
@Import(GlobalExceptionHandler.class)
class UserCommandControllerTest {

    private static final String URL_TEMPLATE = "/api/user/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRestMapper mapper;
    @MockBean
    private ManageUserPort port;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = this.mapperBuilder.featuresToDisable(MapperFeature.USE_ANNOTATIONS).build();
    }

    @Test
    void createUser_ValidRequest_ReturnsCode201() throws Exception {
        UserDto userDto = this.buildTransactionDto();

        Mockito.when(this.mapper.toUserDto(ArgumentMatchers.any())).thenReturn(userDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                                                   .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                                   .content(this.objectMapper.writeValueAsBytes(userDto)))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(this.port).create(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void updateUser_ValidRequest_ReturnsCode200() throws Exception {
        UserDto transactionDto = this.buildTransactionDto();

        Mockito.when(this.mapper.toUserDto(ArgumentMatchers.any())).thenReturn(transactionDto);

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/{username}", "shawneedi")
                                                   .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                                   .content(this.objectMapper.writeValueAsBytes(transactionDto)))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(this.port).save(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    @Test
    void removeUser_ValidRequest_ReturnsCode204() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/{username}", "shawneedi"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.port).removeByUsername(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(this.port);
    }

    private UserDto buildTransactionDto() {
        return UserDto.builder().username("shawneedi").password("ckzr7n1w50001qo136egr7pjm").email("rya_mandellyuf@closure.fi")
                      .fullName("Danyella Montano").build();
    }
}