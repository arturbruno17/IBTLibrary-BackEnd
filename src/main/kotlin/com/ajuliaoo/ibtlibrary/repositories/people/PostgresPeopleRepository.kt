package com.ajuliaoo.ibtlibrary.repositories.people

import com.ajuliaoo.ibtlibrary.models.PeopleDAO
import com.ajuliaoo.ibtlibrary.models.daoToModel
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction

class PostgresPeopleRepository : PeopleRepository {
    override suspend fun insert(name: String, email: String, salt: String, hashPassword: String) =
        suspendTransaction {
            PeopleDAO.new {
                this.name = name
                this.role = role
                this.email = email
                this.salt = salt
                this.hashPassword = hashPassword
            }.daoToModel()
        }
}