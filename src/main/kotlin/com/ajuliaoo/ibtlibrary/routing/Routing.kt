package com.ajuliaoo.ibtlibrary.routing

import com.ajuliaoo.ibtlibrary.routing.auth.authRouting
import com.ajuliaoo.ibtlibrary.routing.books.booksRouting
import com.ajuliaoo.ibtlibrary.repositories.books.BooksRepository
import com.ajuliaoo.ibtlibrary.repositories.loan.LoanRepository
import com.ajuliaoo.ibtlibrary.repositories.loanactivity.LoanActivityRepository
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.routing.loan.loanRouting
import com.ajuliaoo.ibtlibrary.routing.loanactivity.loanActivityRouting
import com.ajuliaoo.ibtlibrary.routing.people.peopleRouting
import com.ajuliaoo.ibtlibrary.routing.summary.summaryRouting
import com.ajuliaoo.ibtlibrary.security.hashing.HashingService
import com.ajuliaoo.ibtlibrary.security.token.JwtTokenService
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    peopleRepository: PeopleRepository,
    booksRepository: BooksRepository,
    loanRepository: LoanRepository,
    loanActivityRepository: LoanActivityRepository,
    tokenService: JwtTokenService
) {
    routing {
        authRouting(
            hashingService = hashingService,
            peopleRepository = peopleRepository,
            tokenService = tokenService
        )
        booksRouting(booksRepository = booksRepository)
        peopleRouting(
            peopleRepository = peopleRepository,
            loanRepository = loanRepository,
        )
        loanRouting(
            booksRepository = booksRepository,
            peopleRepository = peopleRepository,
            loanRepository = loanRepository
        )
        summaryRouting(
            booksRepository = booksRepository,
            peopleRepository = peopleRepository,
            loanRepository = loanRepository
        )
        loanActivityRouting(loanActivityRepository = loanActivityRepository)
        swaggerUI(path = "/swagger")
    }
}
