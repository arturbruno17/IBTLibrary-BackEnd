package com.ajuliaoo.ibtlibrary.routing.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SummaryResponse(
    @SerialName("total_books") val totalBooks: Int,
    @SerialName("active_loans_count") val activeLoansCount: Int,
    @SerialName("readers_count") val readersCount: Int,
    @SerialName("overdue_loans_count") val overdueLoansCount: Int
)
