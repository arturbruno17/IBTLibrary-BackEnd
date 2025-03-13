package com.ajuliaoo.ibtlibrary.repositories.people

import com.ajuliaoo.ibtlibrary.models.Person
import com.ajuliaoo.ibtlibrary.models.Role

interface PeopleRepository {
    suspend fun insert(name: String, role: Role, email: String, salt: String, hashPassword: String): Person
    suspend fun findByEmail(email: String): Person?
}