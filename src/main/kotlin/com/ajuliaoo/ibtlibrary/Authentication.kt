package com.ajuliaoo.ibtlibrary

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    val secret = environment.config.property("ktor.jwt.secret").getString()

    install(Authentication) {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
            )

            validate { jwtCredential ->
                if (jwtCredential.payload.subject.isBlank()) return@validate null
                if (jwtCredential["role"].isNullOrBlank()) return@validate null
                JWTPrincipal(jwtCredential.payload)
            }

            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Forbidden,
                    hashMapOf("message" to "Você não tem as permissões necessárias")
                )
            }
        }
    }
}