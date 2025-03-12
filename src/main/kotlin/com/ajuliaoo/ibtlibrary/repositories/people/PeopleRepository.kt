package com.ajuliaoo.ibtlibrary.repositories.people

import com.ajuliaoo.ibtlibrary.models.Person

interface PeopleRepository {
    suspend fun insert(name: String, email: String, salt: String, hashPassword: String): Person
}