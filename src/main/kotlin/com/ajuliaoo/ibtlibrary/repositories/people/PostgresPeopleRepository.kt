package com.ajuliaoo.ibtlibrary.repositories.people

import com.ajuliaoo.ibtlibrary.database.ilike
import com.ajuliaoo.ibtlibrary.models.*
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or

class PostgresPeopleRepository : PeopleRepository {
    override suspend fun getPeople(query: String?, page: Int, limit: Int): List<Person> = suspendTransaction {
        PeopleDAO.run {
            if (query != null) {
                find {
                    (PeopleTable.name ilike query) or
                    (PeopleTable.email ilike query)
                }
            } else {
                all()
            }.offset((page - 1) * limit.toLong()).limit(limit)
        }.map { it.daoToModel() }
    }

    override suspend fun existsById(id: Int): Boolean = suspendTransaction {
        PeopleDAO.findById(id) != null
    }

    override suspend fun getPersonById(id: Int) =
        suspendTransaction {
            PeopleDAO.findById(id)?.daoToModel()
        }

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

    override suspend fun updatePerson(id: Int, name: String, email: String, role: Role?) = suspendTransaction {
        PeopleDAO.findByIdAndUpdate(id) { person ->
            person.name = name
            person.email = email
            person.role = role ?: person.role
        }?.daoToModel()
    }

    override suspend fun updateRole(id: Int, role: Role) = suspendTransaction {
        PeopleDAO.findByIdAndUpdate(id) {
            it.role = role
        }?.daoToModel()
    }

    override suspend fun findByEmail(email: String): Person? =
        suspendTransaction {
            PeopleDAO.find { PeopleTable.email eq email }
                .firstOrNull()?.daoToModel()
        }

    override suspend fun deleteById(id: Int): Boolean = suspendTransaction {
        PeopleTable.deleteWhere { this.id eq id } == 1
    }
}