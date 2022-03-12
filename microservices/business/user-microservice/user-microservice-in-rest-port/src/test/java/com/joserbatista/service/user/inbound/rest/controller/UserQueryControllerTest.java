package com.joserbatista.service.user.inbound.rest.controller;

import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import com.joserbatista.service.user.core.domain.User;
import com.joserbatista.service.user.core.port.in.GetUserQueryPort;
import com.joserbatista.service.user.inbound.rest.dto.UserDto;
import com.joserbatista.service.user.inbound.rest.mapper.UserRestMapper;
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

import java.util.Optional;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserQueryController.class)
@ContextConfiguration(classes = UserQueryController.class)
@Import(GlobalExceptionHandler.class)
class UserQueryControllerTest {

    private static final String URL_TEMPLATE = "/api/user/{username}/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetUserQueryPort port;
    @MockBean
    private UserRestMapper mapper;

    @Test
    void getUserByUsername_ValidParameters_ReturnsCode200() throws Exception {
        Mockito.when(this.port.getByUsername(ArgumentMatchers.any()))
               .thenReturn(Optional.of(User.builder().username("shawneedi").email("rya_manellyuf@closure.fi").fullName("Danyella Montano").build()));
        Mockito.when(this.mapper.toUserDto(ArgumentMatchers.any()))
               .thenReturn(UserDto.builder().username("shawneedi").email("rya_manellyuf@closure.fi").fullName("Danyella Montano").build());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE, "candancerr0"))
                    .andDo(MockMvcResultHandlers.log())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }
}