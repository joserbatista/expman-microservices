package com.joserbatista.service.importer;

import com.joserbatista.service.common.config.SwaggerUiConfig;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackageClasses = {ImporterServiceApplication.class, SwaggerUiConfig.class, GlobalExceptionHandler.class})
@EnableEurekaClient
public class ImporterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImporterServiceApplication.class, args);
    }

}
