package com.ajuliaoo.ibtlibrary.repositories.loanactivity

import com.ajuliaoo.ibtlibrary.models.Activity
import com.ajuliaoo.ibtlibrary.models.LoanActivity
import java.time.Instant

interface LoanActivityRepository {
    suspend fun createLoanActivity(activity: Activity, loanId: Int)
    suspend fun getAll(maximumDate: Instant?, page: Int, limit: Int): List<LoanActivity>
}