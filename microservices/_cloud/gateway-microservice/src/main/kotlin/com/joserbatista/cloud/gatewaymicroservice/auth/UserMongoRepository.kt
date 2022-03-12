package com.joserbatista.cloud.gatewaymicroservice.auth

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UserMongoRepository : ReactiveCrudRepository<UserDocument, String> {

    fun findByUsername(username: String): Mono<UserDocument>
}

