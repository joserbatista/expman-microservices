package com.joserbatista.cloud.gatewaymicroservice.auth

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import reactor.core.publisher.Mono
import java.io.Closeable

class AuthenticateUserAdapterTest {

    @Mock
    private lateinit var userMongoRepository: UserMongoRepository

    @InjectMocks
    private lateinit var adapter: AuthenticateUserAdapter

    private lateinit var openMocks: AutoCloseable

    @BeforeEach
    fun setUp() {
        openMocks = MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    fun tearDown() {
        openMocks.close()
    }

    @Test
    fun findByUsername_WithUsername_CallsRepository() {
        val userDocument = UserDocument(username = "jaylynmll2", password = "cl0mnc5v70000lc13")
        Mockito.`when`(userMongoRepository.findByUsername(anyString())).thenReturn(Mono.just(userDocument))

        val actual: Mono<UserDetails> = adapter.findByUsername("jaylynmll2")
        val expected: Mono<UserDetails> = Mono.just(User("jaylynmll2", "cl0mnc5v70000lc13", listOf(SimpleGrantedAuthority("user"))))

        Assertions.assertThat(actual.block()).isEqualTo(expected.block())
        Mockito.verify(userMongoRepository).findByUsername(anyString())
    }
}