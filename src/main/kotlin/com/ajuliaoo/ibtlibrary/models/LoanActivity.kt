package com.ajuliaoo.ibtlibrary.models

import com.ajuliaoo.ibtlibrary.serialization.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

enum class Activity { LOAN_CREATED, LOAN_RETURNED, LOAN_EXTENDED }

object LoanActivityTable : IntIdTable("loan_activity") {
    val loan = reference("loan_id", LoanTable)
    val activity = postgresEnumeration<Activity>("activity")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}

class LoanActivityDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LoanActivityDAO>(LoanActivityTable)

    var loan by LoanDAO referencedOn LoanActivityTable.loan
    var activity by LoanActivityTable.activity
    var createdAt by LoanActivityTable.createdAt
}

@Serializable
data class LoanActivity(
    val id: Int,
    val loan: Loan,
    @SerialName("activity") val activity: Activity,
    @Serializable(with = InstantSerializer::class) @SerialName("created_at") val createdAt: Instant,
)

fun LoanActivityDAO.daoToModel(): LoanActivity {
    return LoanActivity(
        id = this.id.value,
        loan = this.loan.daoToModel(),
        activity = this.activity,
        createdAt = this.createdAt,
    )
}