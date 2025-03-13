package com.ajuliaoo.ibtlibrary.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

enum class Role {
    READER, LIBRARIAN, ADMIN
}

object PeopleTable : IntIdTable("people") {
    val name = text("name")
    val role = postgresEnumeration<Role>("role").default(Role.READER)
    val email = text("email").uniqueIndex()
    val salt = text("salt")
    val hashPassword = text("hash_password")
}

class PeopleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PeopleDAO>(PeopleTable)

    var name by PeopleTable.name
    var role by PeopleTable.role
    var email by PeopleTable.email
    var salt by PeopleTable.salt
    var hashPassword by PeopleTable.hashPassword
}

data class Person(
    val id: Int,
    val name: String,
    val role: Role,
    val email: String,
    val salt: String,
    val hashPassword: String,
)

fun PeopleDAO.daoToModel(): Person {
    return Person(
        id = this.id.value,
        name = this.name,
        role = this.role,
        email = this.email,
        salt = this.salt,
        hashPassword = this.hashPassword
    )
}