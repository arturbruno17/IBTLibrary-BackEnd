package com.ajuliaoo.ibtlibrary.repositories.books

import com.ajuliaoo.ibtlibrary.models.Book
import com.ajuliaoo.ibtlibrary.models.BooksDAO
import com.ajuliaoo.ibtlibrary.models.BooksTable
import com.ajuliaoo.ibtlibrary.models.daoToModel
import com.ajuliaoo.ibtlibrary.repositories.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresBooksRepository : BooksRepository {
    override suspend fun getBooks(): List<Book> = suspendTransaction {
        BooksDAO.all().map { it.daoToModel() }
    }

    override suspend fun getBookById(id: Int): Book? = suspendTransaction {
        BooksDAO.findById(id)?.daoToModel()
    }

    override suspend fun insertBook(isbn: String, title: String, author: String, quantity: Int): Book = suspendTransaction {
        BooksDAO.new {
            this.isbn = isbn
            this.title = title
            this.author = author
            this.quantity = quantity
        }.daoToModel()
    }

    override suspend fun updateBook(id: Int, isbn: String, title: String, author: String?, quantity: Int): Book? = suspendTransaction {
        BooksDAO.findByIdAndUpdate(id) {
            it.title = title
            it.isbn = isbn
            it.author = author
            it.quantity = quantity
        }?.daoToModel()
    }

    override suspend fun deleteBook(id: Int) = suspendTransaction {
        BooksTable.deleteWhere { this.id eq id } == 1
    }
}