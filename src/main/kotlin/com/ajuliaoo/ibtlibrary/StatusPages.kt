package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.books.UserIsNotLibrarianException
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                hashMapOf("reasons" to cause.reasons)
            )
        }

        exception<UserIsNotLibrarianException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                hashMapOf("message" to cause.message)
            )
        }
    }
}