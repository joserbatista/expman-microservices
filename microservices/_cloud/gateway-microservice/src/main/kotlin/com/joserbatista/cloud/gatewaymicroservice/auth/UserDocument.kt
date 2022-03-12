package com.joserbatista.cloud.gatewaymicroservice.auth

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("users")
data class UserDocument(

    @field:Id
    val documentId: String? = null,

    @field:Indexed(unique = true)
    val id: String = UUID.randomUUID().toString(),

    @field:Indexed(unique = true)
    val username: String? = null,

    val password: String? = null,

    val enabled: Boolean? = null,
)