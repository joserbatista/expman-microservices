package com.joserbatista.service.transaction.outbound.rest.account.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.joserbatista.service.common.validation.exception.BaseException;
import com.joserbatista.service.common.validation.exception.outbound.OutboundSystemBadRequestException;
import com.joserbatista.service.common.validation.exception.outbound.OutboundSystemUnexpectedErrorException;
import com.joserbatista.service.transaction.core.domain.Account;
import com.joserbatista.service.transaction.core.port.out.GetAccountRemotePort;
import com.joserbatista.service.transaction.outbound.rest.account.client.AccountQueryFeignClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

@SpringBootTest(classes = {GetAccountRestAdapter.class, AccountQueryFeignClientTestBean.class})
@AutoConfigureWireMock(port = 0)
@EnableAutoConfiguration
class GetAccountRestAdapterTest {

    @Autowired
    private AccountQueryFeignClient accountQueryFeignClient;

    @Autowired
    private GetAccountRemotePort adapter;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = this.mapperBuilder.build();
    }

    @Test
    void existsByUsernameAndId_Existing_ReturnsTrue() throws Exception {
        Account account = Account.builder().id("6427d0b8-2ccc-443b-981e-57fe26ba12b8").name("Credit Card").build();

        ResponseDefinitionBuilder responseDefBuilder = WireMock.aResponse().withStatus(HttpStatus.OK.value())
                                                               .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                                               .withBody(this.objectMapper.writeValueAsString(account));

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/user/%s/account/%s".formatted("jencya", "6427d0b8-2ccc-443b-981e-57fe26ba12b8")))
                                 .willReturn(responseDefBuilder));

        boolean actual = this.adapter.existsByUsernameAndId("jencya", "6427d0b8-2ccc-443b-981e-57fe26ba12b8");
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void existsByUsernameAndId_NotExisting_ReturnsFalse() throws BaseException {
        String body = "{\"errorCode\":\"ERR-404\",\"errorMessage\":\"Account <944cfd41-dcb6-4ae1-b749-e2820690d43b> not found\"}";
        ResponseDefinitionBuilder responseDefBuilder = WireMock.aResponse().withStatus(HttpStatus.NOT_FOUND.value())
                                                               .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                                               .withBody(body);

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/user/%s/account/%s".formatted("jencya", "944cfd41-dcb6-4ae1-b749-e2820690d43b")))
                                 .willReturn(responseDefBuilder));

        boolean actual = this.adapter.existsByUsernameAndId("jencya", "944cfd41-dcb6-4ae1-b749-e2820690d43b");
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void existsByUsernameAndId_InternalError_ThrowsOutboundSystemUnexpectedErrorException() {
        String body = "{\"errorCode\":\"ERR-500\",\"errorMessage\":\"Error on remote server\"}";
        ResponseDefinitionBuilder responseDefBuilder = WireMock.aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                               .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                                               .withBody(body);

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/user/%s/account/%s".formatted("jencya", "944cfd41-dcb6-4ae1-b749-e2820690d43b")))
                                 .willReturn(responseDefBuilder));

        Assertions.assertThatExceptionOfType(OutboundSystemUnexpectedErrorException.class)
                  .isThrownBy(() -> this.adapter.existsByUsernameAndId("jencya", "944cfd41-dcb6-4ae1-b749-e2820690d43b"));
    }

    @Test
    void existsByUsernameAndId_BadRequest_ThrowsOutboundSystemBadRequestException() {
        String body = "{\"errorCode\":\"ERR-400\",\"errorMessage\":\"Bad request on remote server\"}";
        ResponseDefinitionBuilder responseDefBuilder = WireMock.aResponse().withStatus(HttpStatus.BAD_REQUEST.value())
                                                               .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                                               .withBody(body);

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/user/%s/account/%s".formatted("jencya", "944cfd41-dcb6-4ae1-b749-e2820690d43b")))
                                 .willReturn(responseDefBuilder));

        Assertions.assertThatExceptionOfType(OutboundSystemBadRequestException.class)
                  .isThrownBy(() -> this.adapter.existsByUsernameAndId("jencya", "944cfd41-dcb6-4ae1-b749-e2820690d43b"));
    }

    @Test
    void findAllByUsername_ValidRequest_ReturnsList() throws Exception {
        List<Account> accountList = List.of(
            Account.builder().id("2c7b2acc-8d3f-45d2-aaeb-d389f8b29b27").name("BCP").build(),
            Account.builder().id("5305dc56-a521-4f2a-bb4b-9589dc3e17d8").name("PayPal").build()
        );
        ResponseDefinitionBuilder responseDefBuilder = WireMock.aResponse().withStatus(HttpStatus.OK.value())
                                                               .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                                               .withBody(this.objectMapper.writeValueAsString(accountList));

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/user/%s/account".formatted("jencya"))).willReturn(responseDefBuilder));

        List<Account> actual = this.adapter.findAllByUsername("jencya");

        Assertions.assertThat(actual).isEqualTo(accountList);
    }
}