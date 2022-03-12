# ExpMan  

[![Maven](https://github.com/joserbatista/expman-microservices/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/joserbatista/expman-microservices/actions/workflows/maven.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=joserbatista_expman-microservices&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=joserbatista_expman-microservices)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=joserbatista_expman-microservices&metric=coverage)](https://sonarcloud.io/summary/new_code?id=joserbatista_expman-microservices)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=joserbatista_expman-microservices&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=joserbatista_expman-microservices)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=joserbatista_expman-microservices&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=joserbatista_expman-microservices)


ExpMan is a project for me to improve my Spring skills.  
  
This page will be updated with further details later on, as the project is still on an early stage.  
  
Please visit my LinkedIn to get to know me better: https://www.linkedin.com/in/joserbatista/en  
  
## Architecture  
  
![](https://user-images.githubusercontent.com/19153705/157264209-7f5379cd-3a04-44db-b022-c5b5c92a6539.png)  
  
## Services  
  
| Service | Language | Description |    
|-|-|-|    
| **[config-server-microservice](microservices/_cloud/config-server-microservice)** |Kotlin| [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config/reference/html/#_spring_cloud_config_server) |    
| **[eureka-server-microservice](microservices/_cloud/eureka-server-microservice)** |Kotlin| [Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery/) |    
| **[gateway-microservice](microservices/_cloud/gateway-microservice)** |Kotlin| [API Gateway](https://spring.io/projects/spring-cloud-gateway) |    
| **[account-microservice](microservices/business/account-microservice)** |Java 17| Hexagonal REST microservice to manage Accounts |    
| **[transaction-microservice](microservices/business/transaction-microservice)** |Java 17| Hexagonal REST microservice to manage Transactions |    
| **[user-microservice](microservices/business/user-microservice)** |Java 17| Hexagonal REST microservice to manage Users |  
| **[import-microservice](microservices/business/import-microservice)** |Java 17| Hexagonal REST microservice to import Accounts and Transactions (currently supports CSV) |    

## Highlights

- **[OpenApiConfig.kt](microservices/_cloud/gateway-microservice/src/main/kotlin/com/joserbatista/cloud/gatewaymicroservice/config/OpenApiConfig.kt)**: create route definitions for routed microservice's swagger definitions
- **[gatewaymicroservice/auth/](microservices/_cloud/gateway-microservice/src/main/kotlin/com/joserbatista/cloud/gatewaymicroservice/auth)**: HTTP _Basic authentication_ implementation using [*MongoReactiveRepository*](https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/repository/ReactiveMongoRepository.html)
- **[ValidateUsernameFilter.kt](microservices/_cloud/gateway-microservice/src/main/kotlin/com/joserbatista/cloud/gatewaymicroservice/auth/ValidateUsernameFilter.kt)**: [*WebFilter*](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/WebFilter.html) to enforce that logged-in User's username matches request paths' username
-- for instance, user *joserbatista* can access URL paths like "*api/user/joserbatista/accounts*" but not "*api/user/johndoe/accounts*", apart from **POST /api/user** to allow registration
- **[AccountMongoRepository.java](microservices/business/account-microservice/account-microservice-out-mongo-port/src/main/java/com/joserbatista/service/account/outbound/mongo/repository/AccountMongoRepository.java)**: *[MongoRepository](https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/repository/MongoRepository.html)* with a [*Aggregation*](https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/repository/Aggregation.html) to calculate the Account's balance
- **[ImportResourceParser.java](microservices/business/import-microservice/import-microservice-core/src/main/java/com/joserbatista/service/importer/core/adapter/parser/ImportResourceParser.java)**: [Strategy](https://refactoring.guru/design-patterns/strategy) class with factory method to handle import based on the input file extension
- **[TransactionQueryControllerTest.java](microservices/business/transaction-microservice/transaction-microservice-in-rest-port/src/test/java/com/joserbatista/service/transaction/inbound/rest/controller/TransactionQueryControllerTest.java)**: [*WebMvcTest*](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.html) that validates if a Controller is acting as expected
- **[TransactionRestMapperTest.java](microservices/business/transaction-microservice/transaction-microservice-in-rest-port/src/test/java/com/joserbatista/service/transaction/inbound/rest/mapper/TransactionRestMapperTest.java)**: Test that checks if a [Mapstruct](https://mapstruct.org/) mapper is behaving as expected
- **[GlobalExceptionHandler.java](common/src/main/java/com/joserbatista/service/common/validation/spring/GlobalExceptionHandler.java)**: Global [*ResponseEntityExceptionHandler*](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html) to handle custom exceptions and some contraint validation exceptions
- **[Uuid.java](common/src/main/java/com/joserbatista/service/common/validation/constraint/Uuid.java)**: Custom [Bean Validation](https://docs.oracle.com/javaee/7/tutorial/bean-validation001.htm) annotation that checks if a String is a valid UUID
