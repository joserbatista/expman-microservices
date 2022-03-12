package com.joserbatista.cloud.gatewaymicroservice.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.regex.Matcher

@Component
class ValidateUsernameFilter : WebFilter {

    companion object {

        private val PATTERN = "/api/user/([^/]+).*".toPattern()
        private val log: Logger = LoggerFactory.getLogger(ValidateUsernameFilter::class.java)
    }

    private fun proceed(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> = chain.filter(exchange)

    private fun fail(): Mono<Void> = Mono.error(AccessDeniedException("Username mismatch"))

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val servletPath: String = request.uri.path

        val matcher = PATTERN.matcher(servletPath)
        if (!matcher.matches() || matcher.groupCount() < 1) {
            log.trace("<{}> did not match regex, proceeding chain", servletPath)
            return proceed(exchange, chain)
        }

        return ReactiveSecurityContextHolder.getContext().map { it.authentication }.flatMap { validateAuth(it, matcher, exchange, chain) }
    }

    private fun validateAuth(authentication: Authentication?, matcher: Matcher, exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if (authentication == null) {
            log.trace("Authentication is null, proceeding chain")
            return proceed(exchange, chain)
        }

        val currentPrincipalName = authentication.name
        log.trace("Validating principal <{}>", currentPrincipalName)
        val username = matcher.group(1)
        if (currentPrincipalName.equals(username, ignoreCase = true)) {
            log.trace("Principal <{}> is valid, proceeding", currentPrincipalName)
            return proceed(exchange, chain)
        }
        log.warn("<{}> is not equal to <{}>, failing", currentPrincipalName, username)
        return fail()
    }
}