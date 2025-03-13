package com.ajuliaoo.ibtlibrary.routing

import com.ajuliaoo.ibtlibrary.models.Role
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*

fun RoutingContext.isUserLibrarian(): Boolean {
    val principal = call.principal<JWTPrincipal>()
    val role = principal!!["role"]!!
    return Role.valueOf(role).ordinal >= Role.LIBRARIAN.ordinal
}

fun RoutingContext.isUserAdmin(): Boolean {
    val principal = call.principal<JWTPrincipal>()
    val role = principal!!["role"]!!
    return Role.valueOf(role).ordinal >= Role.ADMIN.ordinal
}
