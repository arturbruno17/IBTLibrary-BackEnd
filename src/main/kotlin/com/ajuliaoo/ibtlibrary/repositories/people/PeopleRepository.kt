package com.ajuliaoo.ibtlibrary.repositories.people

import com.ajuliaoo.ibtlibrary.models.Person
import com.ajuliaoo.ibtlibrary.models.Role

interface PeopleRepository {
    suspend fun getPeople(query: String?, page: Int, limit: Int): List<Person>
    suspend fun existsById(id: Int): Boolean
    suspend fun getPersonById(id: Int): Person?
    suspend fun insert(name: String, role: Role, email: String, salt: String, hashPassword: String): Person
    suspend fun updatePerson(id: Int, name: String, email: String, role: Role? = null): Person?
    suspend fun updateRole(id: Int, role: Role): Person?
    suspend fun findByEmail(email: String): Person?
    suspend fun deleteById(id: Int): Boolean
}