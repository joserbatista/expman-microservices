package com.joserbatista.cloud.gatewaymicroservice.auth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@WebFluxTest(controllers = [ValidateUsernameFilterTest.TestController::class])
@ContextConfiguration(classes = [ValidateUsernameFilter::class, ValidateUsernameFilterTest.TestController::class])
@ActiveProfiles("test")
internal class ValidateUsernameFilterTest {

    @Autowired
    private lateinit var client: WebTestClient

    @Test
    @WithMockUser(username = "hashimpzvl")
    fun doFilterInternal_MatchingUsername_IsOk() {
        client.get().uri("/api/user/hashimpzvl/test").exchange().expectStatus().isOk
    }

    @Test
    @WithMockUser(username = "sammiej")
    fun doFilterInternal_InvalidUsername_IsForbidden() {
        client.get().uri("/api/user/hashimpzvl/test").exchange().expectStatus().isForbidden
    }

    @Test
    @WithMockUser(username = "sammiej")
    fun doFilterInternal_NonMatchingUrl_IsOk() {
        client.get().uri("/api/user/").exchange().expectStatus().isOk
    }

    @RestController
    class TestController {

        @GetMapping("/api/user/{username}/test")
        fun test(@PathVariable username: String): ResponseEntity<Mono<String>> = ResponseEntity.ok(Mono.just(username))

        @GetMapping("/api/user/")
        fun testUrl(): ResponseEntity<Mono<Void>> = ResponseEntity.ok(Mono.empty())
    }
}