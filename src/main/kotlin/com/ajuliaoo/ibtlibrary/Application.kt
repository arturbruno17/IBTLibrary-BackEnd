package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.repositories.people.PostgresPeopleRepository
import com.ajuliaoo.ibtlibrary.security.hashing.SHA256HashingService
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val peopleRepository = PostgresPeopleRepository()
    val hashingService = SHA256HashingService()

    configureDatabase()
    configureSerialization()
    configureRouting(
        peopleRepository = peopleRepository,
        hashingService = hashingService
    )
    configureValidation()
    configureStatusPages()
}
