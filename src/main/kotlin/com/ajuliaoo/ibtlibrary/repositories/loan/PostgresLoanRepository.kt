package com.ajuliaoo.ibtlibrary.repositories.loan

import com.ajuliaoo.ibtlibrary.models.Loan
import com.ajuliaoo.ibtlibrary.models.LoanDAO
import com.ajuliaoo.ibtlibrary.models.LoanTable
import com.ajuliaoo.ibtlibrary.models.daoToModel
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction
import org.jetbrains.exposed.sql.insertAndGetId
import java.time.Instant

class PostgresLoanRepository : LoanRepository {
    override suspend fun createLoan(personId: Int, bookId: Int): Loan = suspendTransaction {
        val statement = LoanTable.insertAndGetId {
            it[book] = bookId
            it[person] = personId
        }
        LoanDAO.findById(statement)!!.daoToModel()
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