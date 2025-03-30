package com.ajuliaoo.ibtlibrary.routing.loanactivity

import com.ajuliaoo.ibtlibrary.exceptions.InvalidDateException
import com.ajuliaoo.ibtlibrary.exceptions.UserIsNotLibrarianException
import com.ajuliaoo.ibtlibrary.repositories.loanactivity.LoanActivityRepository
import com.ajuliaoo.ibtlibrary.routing.isUserLibrarian
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

fun Routing.loanActivityRouting(loanActivityRepository: LoanActivityRepository) {
    route("/api/v1/loan/activity") {
        authenticate {
            loanActivityRoute(loanActivityRepository = loanActivityRepository)
        }
    }
}

private fun Route.loanActivityRoute(loanActivityRepository: LoanActivityRepository) {
    get {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val maximumDate = try {
            call.parameters["maximum_date"]?.let { LocalDate.parse(it).atStartOfDay().toInstant(ZoneOffset.UTC) }
        } catch (ex: DateTimeParseException) {
            throw InvalidDateException()
        }

        val page = call.parameters["page"]?.toIntOrNull()?.coerceAtLeast(1) ?: 1
        val limit = call.parameters["limit"]?.toIntOrNull()?.coerceAtLeast(1) ?: 25

        val loansActivities = loanActivityRepository.getAll(maximumDate = maximumDate, page = page, limit = limit)
        call.respond(HttpStatusCode.OK, loansActivities)
    }
}