package com.ajuliaoo.ibtlibrary.repositories.loan

import com.ajuliaoo.ibtlibrary.models.Loan

interface LoanRepository {
    suspend fun createLoan(personId: Int, bookId: Int): Loan
    suspend fun activeLoansByBookId(bookId: Int): List<Loan>
    suspend fun returnBook(loanId: Int): Loan?
    suspend fun extendLoan(loanId: Int): Loan?
}
