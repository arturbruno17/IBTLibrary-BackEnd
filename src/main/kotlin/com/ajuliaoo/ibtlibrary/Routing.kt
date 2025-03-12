package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.auth.authRouting
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.security.hashing.HashingService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    peopleRepository: PeopleRepository,
) {
    routing {
        authRouting(
            hashingService = hashingService,
            peopleRepository = peopleRepository
        )
    }
}
