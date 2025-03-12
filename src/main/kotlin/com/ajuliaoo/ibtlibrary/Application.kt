package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.repositories.people.PostgresPeopleRepository
import com.ajuliaoo.ibtlibrary.security.hashing.SHA256HashingService
import com.ajuliaoo.ibtlibrary.security.token.JwtTokenService
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val peopleRepository = PostgresPeopleRepository()
    val hashingService = SHA256HashingService()
    val jwtTokenService = JwtTokenService()

    configureDatabase()
    configureSerialization()
    configureRouting(
        peopleRepository = peopleRepository,
        hashingService = hashingService,
        tokenService = jwtTokenService
    )
    configureValidation()
    configureStatusPages()
}
