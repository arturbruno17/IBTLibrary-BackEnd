package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.exceptions.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable


@Serializable
data class ErrorResponse(
    val message: String,
)

@Serializable
data class BadRequestResponse(
    val reasons: List<String>,
)

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                BadRequestResponse(cause.reasons)
            )
        }

        exception<UserIsNotLibrarianException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(cause.message)
            )
        }

        exception<UserIsNotAdminException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(cause.message)
            )
        }

        exception<InvalidRoleException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                BadRequestResponse(listOf(cause.message))
            )
        }

        exception<ContactAdministratorException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(cause.message)
            )
        }

        exception<BookNotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(cause.message)
            )
        }

        exception<ISBNMustBeUniqueException> { call, cause ->
            call.respond(
                HttpStatusCode.PreconditionFailed,
                ErrorResponse(cause.message)
            )
        }

        exception<UserNotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(cause.message)
            )
        }

        exception<AllInStockWereLoaned> { call, cause ->
            call.respond(
                HttpStatusCode.PreconditionFailed,
                ErrorResponse(cause.message)
            )
        }

        exception<PersonAlreadyLoanedTheBook> { call, cause ->
            call.respond(
                HttpStatusCode.PreconditionFailed,
                ErrorResponse(cause.message)
            )

        }

        exception<InvalidLoanTypeException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                BadRequestResponse(listOf(cause.message))
            )
        }

        exception<InvalidDateException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                BadRequestResponse(listOf(cause.message))
            )
        }
    }
}