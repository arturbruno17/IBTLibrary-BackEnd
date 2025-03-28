package com.ajuliaoo.ibtlibrary.routing.people

import com.ajuliaoo.ibtlibrary.exceptions.*
import com.ajuliaoo.ibtlibrary.models.Role
import com.ajuliaoo.ibtlibrary.repositories.loan.LoanRepository
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.routing.isUserAdmin
import com.ajuliaoo.ibtlibrary.routing.isUserLibrarian
import com.ajuliaoo.ibtlibrary.routing.people.update.request.UpdatePersonDto
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.peopleRouting(
    peopleRepository: PeopleRepository,
    loanRepository: LoanRepository,
) {
    route("/api/v1/people") {
        authenticate {
            getAllPeopleRoute(peopleRepository = peopleRepository)
            getPersonByIdRoute(peopleRepository = peopleRepository)
            getLoansByPersonIdRoute(
                loanRepository = loanRepository,
                peopleRepository = peopleRepository
            )
            updatePersonByIdRoute(peopleRepository = peopleRepository)
            turnPersonInDifferentRoleRoute(peopleRepository = peopleRepository)
            deletePersonByIdRoute(peopleRepository = peopleRepository)
        }
    }
}

private fun Route.getAllPeopleRoute(
    peopleRepository: PeopleRepository
) {
    get {
        val query = call.queryParameters["q"]
        val page = call.queryParameters["page"]?.toIntOrNull() ?: 1
        val limit = call.queryParameters["limit"]?.toIntOrNull() ?: 25
        val people = peopleRepository.getPeople(query = query, page = page, limit = limit)
        call.respond(HttpStatusCode.OK, people)
    }
}

private fun Route.getPersonByIdRoute(
    peopleRepository: PeopleRepository
) {
    get("/{id}") {
        val id = call.parameters["id"]!!.toInt()
        val person = peopleRepository.getPersonById(id)
        person
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
}

private fun Route.getLoansByPersonIdRoute(
    loanRepository: LoanRepository,
    peopleRepository: PeopleRepository
) {
    get("/{id}/loans") {
        val id = call.parameters["id"]!!.toInt()

        val existsPerson = peopleRepository.existsById(id)
        if (!existsPerson) throw UserNotFoundException()

        val principal = call.principal<JWTPrincipal>()!!
        val requesterId = principal.subject!!.toInt()

        if (requesterId != id && !isUserLibrarian()) {
            throw UserIsNotLibrarianException()
        }

        val loans = loanRepository.getAllLoans(personId = id)
        call.respond(HttpStatusCode.OK, loans)
    }
}

private fun Route.updatePersonByIdRoute(
    peopleRepository: PeopleRepository
) {
    put("/{id}") {
        val id = call.parameters["id"]!!.toInt()
        val updateDto = call.receive<UpdatePersonDto>()

        val principal = call.principal<JWTPrincipal>()
        val requesterId = principal!!.subject!!.toInt()
        if (requesterId == id) {
            peopleRepository.updatePerson(
                id = id,
                name = updateDto.name,
                email = updateDto.email
            )
        } else {
            if (!isUserAdmin()) throw UserIsNotAdminException()
            peopleRepository.updatePerson(
                id = id,
                role = updateDto.role,
                name = updateDto.name,
                email = updateDto.email
            )
        }
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
}

private fun Route.turnPersonInDifferentRoleRoute(
    peopleRepository: PeopleRepository
) {
    patch("/{id}/{role}") {
        if (!isUserAdmin()) throw UserIsNotAdminException()

        val chosenRole = try {
            Role.valueOf(call.parameters["role"]!!)
        } catch (e: IllegalArgumentException) {
            throw InvalidRoleException()
        }

        if (chosenRole !in listOf(Role.READER, Role.LIBRARIAN)) {
            throw ContactAdministratorException()
        }

        val personId = call.parameters["id"]!!.toInt()
        peopleRepository.updateRole(personId, chosenRole)
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
}

private fun Route.deletePersonByIdRoute(
    peopleRepository: PeopleRepository
) {
    delete("/{id}") {
        if (!isUserAdmin()) throw UserIsNotAdminException()

        val id = call.parameters["id"]!!.toInt()
        val deleted = peopleRepository.deleteById(id)
        if (deleted) call.respond(HttpStatusCode.NoContent)
        else call.respond(HttpStatusCode.NotFound)
    }
}