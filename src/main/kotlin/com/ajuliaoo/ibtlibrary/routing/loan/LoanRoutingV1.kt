package com.ajuliaoo.ibtlibrary.routing.loan

import com.ajuliaoo.ibtlibrary.exceptions.BookNotFoundException
import com.ajuliaoo.ibtlibrary.exceptions.UserIsNotLibrarianException
import com.ajuliaoo.ibtlibrary.repositories.loan.LoanRepository
import com.ajuliaoo.ibtlibrary.routing.isUserLibrarian
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Routing.loanRouting(loanRepository: LoanRepository) {
    route("/api/v1/loan") {
        authenticate {
            createLoanRoute(loanRepository = loanRepository)
            extendLoanRoute(loanRepository = loanRepository)
            returnBookRoute(loanRepository = loanRepository)
        }
    }
}

private fun Route.createLoanRoute(
    loanRepository: LoanRepository
) {
    // TODO: Adicionar validações diferentes para personId e bookId
    // TODO: Relacionar a quantidade de livros em estoque com a quantidade de livros emprestados
    // TODO: Não permitir que um usuário faça o empréstimo do mesmo livro ao mesmo tempo mais de 1x
    post("/{bookId}/{personId}") {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val bookId = call.parameters["bookId"]!!.toInt()
        val personId = call.parameters["personId"]!!.toInt()

        try {
            val loan = loanRepository.createLoan(
                bookId = bookId,
                personId = personId
            )
            call.respond(HttpStatusCode.Created, loan)
        } catch (ex: ExposedSQLException) {
            throw BookNotFoundException()
        }
    }
}

private fun Route.extendLoanRoute(
    loanRepository: LoanRepository,
) {
    patch("/{loanId}/extend") {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val loanId = call.parameters["loanId"]!!.toInt()

        loanRepository.extendLoan(loanId)
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
}

private fun Route.returnBookRoute(
    loanRepository: LoanRepository
) {
    patch("/{loanId}/return") {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val loanId = call.parameters["loanId"]!!.toInt()
        loanRepository.returnBook(loanId)
            ?.let {  call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
}