package com.ajuliaoo.ibtlibrary.routing.loan

import com.ajuliaoo.ibtlibrary.exceptions.*
import com.ajuliaoo.ibtlibrary.models.Loan
import com.ajuliaoo.ibtlibrary.repositories.books.BooksRepository
import com.ajuliaoo.ibtlibrary.repositories.loan.LoanRepository
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.routing.isUserLibrarian
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.loanRouting(
    loanRepository: LoanRepository,
    booksRepository: BooksRepository,
    peopleRepository: PeopleRepository
) {
    route("/api/v1/loan") {
        authenticate {
            getAllLoans(loanRepository = loanRepository)
            createLoanRoute(
                peopleRepository = peopleRepository,
                booksRepository = booksRepository,
                loanRepository = loanRepository
            )
            extendLoanRoute(loanRepository = loanRepository)
            returnBookRoute(loanRepository = loanRepository)
        }
    }
}

private fun Route.getAllLoans(loanRepository: LoanRepository) {
    get {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val personId = call.queryParameters["person_id"]?.toInt()
        val bookId = call.queryParameters["book_id"]?.toInt()

        val types = try {
            call.queryParameters["types"]?.split(",")
                ?.map { Loan.Type.valueOf(it.uppercase()) }
        } catch (ex: IllegalArgumentException) {
            throw InvalidLoanTypeException()
        }

        val loans = loanRepository.getAllLoans(bookId = bookId, personId = personId, types = types)
        call.respond(HttpStatusCode.OK, loans)
    }
}

private fun Route.createLoanRoute(
    peopleRepository: PeopleRepository,
    booksRepository: BooksRepository,
    loanRepository: LoanRepository
) {
    post("/{bookId}/{personId}") {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val bookId = call.parameters["bookId"]!!.toInt()
        val personId = call.parameters["personId"]!!.toInt()

        if (!peopleRepository.existsById(personId)) throw UserNotFoundException()

        val book = booksRepository.getBookById(bookId) ?: throw BookNotFoundException()
        val activeLoansByBookId = loanRepository.activeLoansByBookId(bookId)

        if (activeLoansByBookId.size >= book.quantity) throw AllInStockWereLoaned()
        if (activeLoansByBookId.any { it.person.id == personId }) throw PersonAlreadyLoanedTheBook(book.title)

        val loan = loanRepository.createLoan(
            bookId = bookId,
            personId = personId
        )
        call.respond(HttpStatusCode.Created, loan)
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
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
}