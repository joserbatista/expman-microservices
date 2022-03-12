package com.joserbatista.service.transaction.outbound.rest.account;

import com.joserbatista.service.transaction.outbound.rest.account.client.CustomFeignErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = FeignConfig.class)
public class FeignConfig {

    @Bean
    public Feign.Builder feignBuilder() {
        return this.customizeBuilder(Feign.builder());
    }

    public Feign.Builder customizeBuilder(Feign.Builder builder) {
        return builder.retryer(Retryer.NEVER_RETRY).errorDecoder(new CustomFeignErrorDecoder()).decode404().logLevel(Logger.Level.BASIC);
    }
}
