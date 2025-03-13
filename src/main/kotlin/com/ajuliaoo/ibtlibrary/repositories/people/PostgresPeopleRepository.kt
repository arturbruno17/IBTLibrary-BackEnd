package com.ajuliaoo.ibtlibrary.repositories.people

import com.ajuliaoo.ibtlibrary.models.*
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction

class PostgresPeopleRepository : PeopleRepository {
    override suspend fun insert(name: String, role: Role, email: String, salt: String, hashPassword: String) =
        suspendTransaction {
            PeopleDAO.new {
                this.name = name
                this.role = role
                this.email = email
                this.salt = salt
                this.hashPassword = hashPassword
            }.daoToModel()
        }

    override suspend fun findByEmail(email: String): Person? =
        suspendTransaction {
            PeopleDAO.find { PeopleTable.email eq email }
                .firstOrNull()?.daoToModel()
        }
}