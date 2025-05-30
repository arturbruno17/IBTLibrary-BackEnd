package com.ajuliaoo.ibtlibrary.repositories.books

import com.ajuliaoo.ibtlibrary.models.Book


interface BooksRepository {
    suspend fun getBooks(query: String?, page: Int, limit: Int): List<Book>
    suspend fun existsById(id: Int): Boolean
    suspend fun countBooks(): Int
    suspend fun getBookById(id: Int): Book?
    suspend fun insertBook(isbn: String, title: String, author: String?, quantity: Int): Book
    suspend fun updateBook(id: Int, isbn: String, title: String, author: String?, quantity: Int): Book?
    suspend fun deleteBook(id: Int): Boolean
}