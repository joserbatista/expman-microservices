package com.joserbatista.cloud.gatewaymicroservice

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication(scanBasePackageClasses = [GatewayMicroserviceApplication::class])
@EnableEurekaClient
@EnableReactiveMongoRepositories(basePackageClasses = [GatewayMicroserviceApplication::class])
@OpenAPIDefinition(info = Info(title = "Gateway API", version = "1.0"))
class GatewayMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<GatewayMicroserviceApplication>(*args)
}
