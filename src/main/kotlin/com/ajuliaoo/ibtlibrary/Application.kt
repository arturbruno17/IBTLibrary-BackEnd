package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.repositories.books.PostgresBooksRepository
import com.ajuliaoo.ibtlibrary.repositories.loan.PostgresLoanRepository
import com.ajuliaoo.ibtlibrary.repositories.people.PostgresPeopleRepository
import com.ajuliaoo.ibtlibrary.routing.configureRouting
import com.ajuliaoo.ibtlibrary.security.hashing.SHA256HashingService
import com.ajuliaoo.ibtlibrary.security.token.JwtTokenService
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val peopleRepository = PostgresPeopleRepository()
    val booksRepository = PostgresBooksRepository()
    val loanRepository = PostgresLoanRepository()

    val hashingService = SHA256HashingService()
    val jwtTokenService = JwtTokenService()

    configureDatabase()
    configureSerialization()
    configureAuthentication()
    configureValidation()
    configureStatusPages()
    configureRouting(
        peopleRepository = peopleRepository,
        booksRepository = booksRepository,
        loanRepository = loanRepository,
        hashingService = hashingService,
        tokenService = jwtTokenService
    )
}
