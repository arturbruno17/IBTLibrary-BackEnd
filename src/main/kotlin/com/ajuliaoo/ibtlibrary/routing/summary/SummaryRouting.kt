package com.ajuliaoo.ibtlibrary.routing.summary

import com.ajuliaoo.ibtlibrary.exceptions.UserIsNotLibrarianException
import com.ajuliaoo.ibtlibrary.models.Loan
import com.ajuliaoo.ibtlibrary.repositories.books.BooksRepository
import com.ajuliaoo.ibtlibrary.repositories.loan.LoanRepository
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.routing.isUserLibrarian
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.summaryRouting(
    booksRepository: BooksRepository,
    peopleRepository: PeopleRepository,
    loanRepository: LoanRepository
) {
    route("/api/v1/summary") {
        authenticate {
            summaryRoute(
                booksRepository = booksRepository,
                peopleRepository = peopleRepository,
                loanRepository = loanRepository
            )
        }
    }
}

private fun Route.summaryRoute(
    booksRepository: BooksRepository,
    peopleRepository: PeopleRepository,
    loanRepository: LoanRepository
) {
    get {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val summary = SummaryResponse(
            totalBooks = booksRepository.countBooks(),
            activeLoansCount = loanRepository.countLoansByTypes(types = listOf(Loan.Type.IN_DAYS, Loan.Type.OVERDUE)),
            readersCount = peopleRepository.countReaders(),
            overdueLoansCount = loanRepository.countLoansByTypes(types = listOf(Loan.Type.OVERDUE))
        )

        call.respond(HttpStatusCode.OK, summary)
    }
}