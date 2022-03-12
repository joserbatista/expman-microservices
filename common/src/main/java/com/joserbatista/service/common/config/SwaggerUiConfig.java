package com.joserbatista.service.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerUiConfig {

    @Bean
    public OpenAPI customOpenApi(@Value("${spring.application.name}") String name, @Value("${spring.application.version:}") String version) {
        return new OpenAPI().components(new Components()).info(new Info().title(name).version(version));
    }

}
