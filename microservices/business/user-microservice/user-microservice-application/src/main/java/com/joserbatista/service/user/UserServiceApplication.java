package com.joserbatista.service.user;

import com.joserbatista.service.common.config.SwaggerUiConfig;
import com.joserbatista.service.common.validation.spring.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackageClasses = {UserServiceApplication.class, SwaggerUiConfig.class, GlobalExceptionHandler.class})
@EnableEurekaClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
