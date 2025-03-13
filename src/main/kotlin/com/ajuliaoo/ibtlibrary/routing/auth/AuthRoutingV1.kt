package com.ajuliaoo.ibtlibrary.routing.auth

import com.ajuliaoo.ibtlibrary.ErrorResponse
import com.ajuliaoo.ibtlibrary.exceptions.UserIsNotAdminException
import com.ajuliaoo.ibtlibrary.models.Role
import com.ajuliaoo.ibtlibrary.routing.auth.login.request.LoginDto
import com.ajuliaoo.ibtlibrary.routing.auth.login.response.LoginResponse
import com.ajuliaoo.ibtlibrary.routing.auth.register.request.RegisterDto
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.routing.isUserAdmin
import com.ajuliaoo.ibtlibrary.security.hashing.HashingService
import com.ajuliaoo.ibtlibrary.security.hashing.SaltedHash
import com.ajuliaoo.ibtlibrary.security.token.TokenClaim
import com.ajuliaoo.ibtlibrary.security.token.TokenConfig
import com.ajuliaoo.ibtlibrary.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.auth.*
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
        authenticate {
            registerLibrarianRoute(hashingService = hashingService, peopleRepository = peopleRepository)
        }
    }
}

private fun Route.registerRoute(
    hashingService: HashingService,
    peopleRepository: PeopleRepository
) {
    post("/register") {
        val registerDto = call.receive<RegisterDto>()
        try {
            registerUserAs(
                role = Role.READER,
                registerDto = registerDto,
                hashingService = hashingService,
                peopleRepository = peopleRepository
            )
            call.respond(HttpStatusCode.Created)
        } catch (ex: ExposedSQLException) {
            call.respond(
                HttpStatusCode.PreconditionFailed,
                ErrorResponse("E-mail encontrado no nosso banco de dados")
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
                ErrorResponse("E-mail não encontrado no nosso banco de dados")
            )
            return@post
        }

        val matchPassword = hashingService.verify(loginDto.password, SaltedHash(person.hashPassword, person.salt))
        if (!matchPassword) {
            call.respond(
                HttpStatusCode.PreconditionFailed,
                ErrorResponse("Credenciais inválidas")
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

private fun Route.registerLibrarianRoute(
    hashingService: HashingService,
    peopleRepository: PeopleRepository,
) {
    post("/register/librarian") {
        if (!isUserAdmin()) throw UserIsNotAdminException()

        val registerDto = call.receive<RegisterDto>()

        try {
            registerUserAs(
                role = Role.LIBRARIAN,
                registerDto = registerDto,
                hashingService = hashingService,
                peopleRepository = peopleRepository
            )
            call.respond(HttpStatusCode.Created)
        } catch (ex: ExposedSQLException) {
            call.respond(
                HttpStatusCode.PreconditionFailed,
                ErrorResponse("E-mail encontrado no nosso banco de dados")
            )
        }
    }
}

private suspend fun registerUserAs(
    role: Role,
    registerDto: RegisterDto,
    peopleRepository: PeopleRepository,
    hashingService: HashingService
) {
    val saltedHash = hashingService.generateSaltedHash(registerDto.password)
    peopleRepository.insert(
        name = registerDto.name,
        role = role,
        email = registerDto.email,
        salt = saltedHash.salt,
        hashPassword = saltedHash.hash
    )
}