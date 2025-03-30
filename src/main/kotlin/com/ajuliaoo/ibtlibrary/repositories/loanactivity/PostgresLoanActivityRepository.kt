package com.ajuliaoo.ibtlibrary.repositories.loanactivity

import com.ajuliaoo.ibtlibrary.models.*
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.insert
import java.time.Instant

class PostgresLoanActivityRepository : LoanActivityRepository {
    override suspend fun createLoanActivity(activityType: ActivityType, loanId: Int): Unit = suspendTransaction {
        LoanActivityTable.insert {
            it[this.activityType] = activityType
            it[this.loan] = loanId
        }
    }

    override suspend fun getAll(maximumDate: Instant?, page: Int, limit: Int): List<LoanActivity> = suspendTransaction {
        LoanActivityDAO.find {
            maximumDate?.let { LoanActivityTable.createdAt greaterEq it } ?: Op.TRUE
        }.offset((page - 1) * limit.toLong()).limit(limit).map { it.daoToModel() }
    }
}