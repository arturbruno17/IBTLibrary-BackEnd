package com.ajuliaoo.ibtlibrary.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object BooksTable: IntIdTable("books") {
    val isbn = varchar("isbn", 13).uniqueIndex()
    val title = text("title")
    val author = text("author").nullable()
    val quantity = integer("quantity").default(1)
}

class BooksDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BooksDAO>(BooksTable)

    var isbn by BooksTable.isbn
    var title by BooksTable.title
    var author by BooksTable.author
    var quantity by BooksTable.quantity
}

@Serializable
data class Book(
    val id: Int,
    val isbn: String,
    val title : String,
    val author: String?,
    val quantity: Int,
)

fun BooksDAO.daoToModel(): Book {
    return Book(
        id = this.id.value,
        isbn = this.isbn,
        title = this.title,
        author = this.author,
        quantity = this.quantity
    )
}
