package com.ajuliaoo.ibtlibrary.repositories.books

import com.ajuliaoo.ibtlibrary.models.Book


interface BooksRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBookById(id: Int): Book?
    suspend fun insertBook(isbn: String, title: String, author: String, quantity: Int): Book
    suspend fun updateBook(id: Int, isbn: String, title: String, author: String?, quantity: Int): Book?
    suspend fun deleteBook(id: Int): Boolean
}