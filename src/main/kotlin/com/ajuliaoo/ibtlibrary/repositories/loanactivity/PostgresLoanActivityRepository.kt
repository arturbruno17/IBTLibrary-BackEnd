package com.ajuliaoo.ibtlibrary.repositories.loanactivity

import com.ajuliaoo.ibtlibrary.models.*
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import java.time.Instant

class PostgresLoanActivityRepository : LoanActivityRepository {
    override suspend fun createLoanActivity(activity: Activity, loanId: Int): Unit = suspendTransaction {
        LoanActivityTable.insert {
            it[this.activity] = activity
            it[this.loan] = loanId
        }
    }

    override suspend fun getAll(maximumDate: Instant?, page: Int, limit: Int): List<LoanActivity> = suspendTransaction {
        LoanActivityDAO.find {
            maximumDate?.let { LoanActivityTable.createdAt greaterEq it } ?: Op.TRUE
        }
            .offset((page - 1) * limit.toLong())
            .limit(limit)
            .orderBy(LoanActivityTable.createdAt to SortOrder.DESC)
            .map { it.daoToModel() }
    }
}