package com.ajuliaoo.ibtlibrary.routing.people

import com.ajuliaoo.ibtlibrary.exceptions.ContactAdministratorException
import com.ajuliaoo.ibtlibrary.exceptions.InvalidRoleException
import com.ajuliaoo.ibtlibrary.exceptions.UserIsNotAdminException
import com.ajuliaoo.ibtlibrary.models.Role
import com.ajuliaoo.ibtlibrary.repositories.people.PeopleRepository
import com.ajuliaoo.ibtlibrary.routing.isUserAdmin
import com.ajuliaoo.ibtlibrary.routing.people.update.request.UpdatePersonDto
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.peopleRouting(
    peopleRepository: PeopleRepository,
) {
    route("/api/v1/people") {
        authenticate {
            getAllPeopleRoute(peopleRepository = peopleRepository)
            getPersonByIdRoute(peopleRepository = peopleRepository)
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
        val people = peopleRepository.getPeople()
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