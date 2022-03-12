package com.joserbatista.service.transaction;

import com.joserbatista.service.common.config.SwaggerUiConfig;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackageClasses = {TransactionServiceApplication.class, SwaggerUiConfig.class, GlobalExceptionHandler.class})
@EnableEurekaClient
public class TransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

}
