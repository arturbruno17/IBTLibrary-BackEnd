package com.ajuliaoo.ibtlibrary.routing.people.response

import com.ajuliaoo.ibtlibrary.models.Person
import com.ajuliaoo.ibtlibrary.models.Role
import kotlinx.serialization.Serializable

@Serializable
data class PersonResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: Role,
)

fun Person.toDto(): PersonResponse {
    return PersonResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        role = this.role
    )
}