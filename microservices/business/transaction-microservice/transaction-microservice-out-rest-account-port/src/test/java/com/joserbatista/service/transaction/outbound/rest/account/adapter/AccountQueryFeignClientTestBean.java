package com.joserbatista.service.transaction.outbound.rest.account.adapter;

import com.joserbatista.service.transaction.outbound.rest.account.FeignConfig;
import com.joserbatista.service.transaction.outbound.rest.account.client.AccountQueryFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class AccountQueryFeignClientTestBean {

    @Bean
    public AccountQueryFeignClient create(
        ApplicationContext applicationContext, @Value("${wiremock.server.port}") String wiremockPort
    ) {
        FeignConfig feignConfig = new FeignConfig();

        return new FeignClientBuilder(applicationContext).forType(AccountQueryFeignClient.class, "account-microservice")
                                                         .url("http://localhost:%s".formatted(wiremockPort))
                                                         .customize(feignConfig::customizeBuilder)
                                                         .build();
    }
}
