package com.ajuliaoo.ibtlibrary.auth

import com.ajuliaoo.ibtlibrary.auth.register.request.RegisterDto
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.security.hashing.HashingService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Routing.authRouting(
    hashingService: HashingService,
    peopleRepository: PeopleRepository
) {
    route("/api/v1/auth") {
        registerRoute(hashingService = hashingService, peopleRepository = peopleRepository)
    }
}

private fun Route.registerRoute(
    hashingService: HashingService,
    peopleRepository: PeopleRepository
) {
    post("/register") {
        val registerDto = call.receive<RegisterDto>()
        val saltedHash = hashingService.generateSaltedHash(registerDto.password)
        try {
            peopleRepository.insert(
                name = registerDto.name,
                email = registerDto.email,
                salt = saltedHash.salt,
                hashPassword = saltedHash.hash
            )
            call.respond(HttpStatusCode.Created)
        } catch (ex: ExposedSQLException) {
            call.respond(
                HttpStatusCode.PreconditionFailed,
                hashMapOf("message" to "E-mail encontrado no nosso banco de dados")
            )
        }
    }
}