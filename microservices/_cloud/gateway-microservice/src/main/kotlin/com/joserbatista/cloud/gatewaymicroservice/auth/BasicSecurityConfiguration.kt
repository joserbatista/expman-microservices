package com.joserbatista.cloud.gatewaymicroservice.auth

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class BasicSecurityConfiguration {

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.csrf().disable()
            .authorizeExchange().pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .pathMatchers(HttpMethod.POST, "/api/user").permitAll()
            .anyExchange().authenticated()
            .and().httpBasic().and().build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}