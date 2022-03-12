package com.joserbatista.service.account;

import com.joserbatista.service.common.config.SwaggerUiConfig;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackageClasses = {AccountServiceApplication.class, SwaggerUiConfig.class, GlobalExceptionHandler.class})
@EnableEurekaClient
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
