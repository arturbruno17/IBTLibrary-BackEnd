package com.ajuliaoo.ibtlibrary.repositories.loan

import com.ajuliaoo.ibtlibrary.database.sumDays
import com.ajuliaoo.ibtlibrary.models.Loan
import com.ajuliaoo.ibtlibrary.models.LoanDAO
import com.ajuliaoo.ibtlibrary.models.LoanTable
import com.ajuliaoo.ibtlibrary.models.daoToModel
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.or
import java.time.Instant

class PostgresLoanRepository : LoanRepository {
    private fun isOverdue(): Op<Boolean> =
        LoanTable.returnDate.isNull() and
                (CurrentTimestamp greater (LoanTable.startDate sumDays LoanTable.duration))

    private fun isInDays(): Op<Boolean> =
        LoanTable.returnDate.isNull() and
                (CurrentTimestamp lessEq (LoanTable.startDate sumDays LoanTable.duration))

    private fun isReturned(): Op<Boolean> = LoanTable.returnDate.isNotNull()

    override suspend fun getAllLoans(
        bookId: Int?,
        personId: Int?,
        types: List<Loan.Type>?,
        page: Int,
        limit: Int
    ): List<Loan> =
        suspendTransaction {
            LoanDAO.run {
                find {
                    var condition: Op<Boolean> = Op.TRUE
                    bookId?.let { condition = condition and (LoanTable.book eq it) }
                    personId?.let { condition = condition and (LoanTable.person eq it) }

                    types?.let {
                        val nestedCondition = buildList {
                            if (Loan.Type.IN_DAYS in it) add(isInDays())
                            if (Loan.Type.OVERDUE in it) add(isOverdue())
                            if (Loan.Type.RETURNED in it) add(isReturned())
                        }.takeIf { it.isNotEmpty() }?.reduce { acc, op -> acc or op }

                        if (nestedCondition != null) condition = condition and nestedCondition
                    }

                    condition
                }.offset((page - 1) * limit.toLong()).limit(limit)
            }.map { it.daoToModel() }
        }

    override suspend fun createLoan(personId: Int, bookId: Int): Loan = suspendTransaction {
        val statement = LoanTable.insertAndGetId {
            it[book] = bookId
            it[person] = personId
        }
        LoanDAO.findById(statement)!!.daoToModel()
    }

    override suspend fun activeLoansByBookId(bookId: Int): List<Loan> = suspendTransaction {
        LoanDAO.find { (LoanTable.book eq bookId) and (LoanTable.returnDate eq null) }.map { it.daoToModel() }
    }

    override suspend fun returnBook(loanId: Int): Loan? = suspendTransaction {
        LoanDAO.findByIdAndUpdate(loanId) {
            it.returnDate = Instant.now()
        }?.daoToModel()
    }

    override suspend fun extendLoan(loanId: Int): Loan? = suspendTransaction {
        LoanDAO.findByIdAndUpdate(loanId) { it.duration += 15 }
            ?.daoToModel()
    }
}