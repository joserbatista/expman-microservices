package com.joserbatista.cloud.gatewaymicroservice.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthenticateUserAdapter(private val userMongoRepository: UserMongoRepository) : ReactiveUserDetailsService {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun findByUsername(username: String): Mono<UserDetails> {
        log.trace("Authenticating user <$username>")
        return userMongoRepository.findByUsername(username).mapNotNull { User(it.username, it.password, listOf(SimpleGrantedAuthority("user"))) }
    }

}