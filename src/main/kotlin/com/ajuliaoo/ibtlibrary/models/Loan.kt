package com.ajuliaoo.ibtlibrary.models

import kotlinx.serialization.SerialName
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object LoanTable : IntIdTable("loan") {
    val person = reference("people_id", PeopleTable)
    val book = reference("book_id", BooksTable)
    val startDate = timestamp("start_date").defaultExpression(CurrentTimestamp)
    val duration = integer("duration").default(15)
    val returnDate = timestamp("return_date").nullable()
}

class LoanDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LoanDAO>(LoanTable)

    var person by PeopleDAO referencedOn LoanTable.person
    var book by BooksDAO referencedOn LoanTable.book
    var startDate by LoanTable.startDate
    var duration by LoanTable.duration
    var returnDate by LoanTable.returnDate
}

data class Loan(
    val person: Person,
    val book: Book,
    @SerialName("start_date") val startDate: Instant,
    val duration: Int,
    @SerialName("return_date") val returnDate: Instant?,
)

fun LoanDAO.daoToModel(): Loan {
    return Loan(
        person = this.person.daoToModel(),
        book = this.book.daoToModel(),
        startDate = this.startDate,
        duration = this.duration,
        returnDate = this.returnDate,
    )
}