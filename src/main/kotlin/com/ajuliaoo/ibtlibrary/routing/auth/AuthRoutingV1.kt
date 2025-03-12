package com.ajuliaoo.ibtlibrary.routing.auth

import com.ajuliaoo.ibtlibrary.routing.auth.login.request.LoginDto
import com.ajuliaoo.ibtlibrary.routing.auth.login.response.LoginResponse
import com.ajuliaoo.ibtlibrary.routing.auth.register.request.RegisterDto
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.security.hashing.HashingService
import com.ajuliaoo.ibtlibrary.security.hashing.SaltedHash
import com.ajuliaoo.ibtlibrary.security.token.TokenClaim
import com.ajuliaoo.ibtlibrary.security.token.TokenConfig
import com.ajuliaoo.ibtlibrary.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Routing.authRouting(
    hashingService: HashingService,
    peopleRepository: PeopleRepository,
    tokenService: TokenService
) {
    route("/api/v1/auth") {
        registerRoute(hashingService = hashingService, peopleRepository = peopleRepository)
        loginRoute(hashingService = hashingService, peopleRepository = peopleRepository, tokenService = tokenService)
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

private fun Route.loginRoute(
    hashingService: HashingService,
    peopleRepository: PeopleRepository,
    tokenService: TokenService
) {
    post("/login") {
        val loginDto = call.receive<LoginDto>()

        val person = peopleRepository.findByEmail(loginDto.email)
        if (person == null) {
            call.respond(
                HttpStatusCode.PreconditionFailed,
                hashMapOf("message" to "E-mail não encontrado no nosso banco de dados")
            )
            return@post
        }

        val matchPassword = hashingService.verify(loginDto.password, SaltedHash(person.hashPassword, person.salt))
        if (!matchPassword) {
            call.respond(
                HttpStatusCode.PreconditionFailed,
                hashMapOf("message" to "Credenciais inválidas")
            )
        }

        val tokenConfig = TokenConfig(
            secret = environment.config.property("ktor.jwt.secret").getString(),
            expiresAt = environment.config.property("ktor.jwt.expiresAt").getString().toLong(),
            subject = person.id.toString()
        )

        val (accessToken, refreshToken) = tokenService.generate(
            config = tokenConfig,
            TokenClaim("email", person.email),
            TokenClaim("role", person.role.name)
        )
        call.respond(HttpStatusCode.OK, LoginResponse(accessToken, refreshToken))
    }
}

private fun Route.registerLibrarianRoute() {
    TODO("Not implemented yet")
}