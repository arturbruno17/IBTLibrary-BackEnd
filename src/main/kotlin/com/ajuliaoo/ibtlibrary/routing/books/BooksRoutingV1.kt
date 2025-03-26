package com.ajuliaoo.ibtlibrary.routing.books

import com.ajuliaoo.ibtlibrary.exceptions.ISBNMustBeUniqueException
import com.ajuliaoo.ibtlibrary.exceptions.UserIsNotLibrarianException
import com.ajuliaoo.ibtlibrary.routing.books.update.request.BookDto
import com.ajuliaoo.ibtlibrary.repositories.books.BooksRepository
import com.ajuliaoo.ibtlibrary.routing.isUserLibrarian
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Routing.booksRouting(booksRepository: BooksRepository) {
    route("/api/v1/books") {
        authenticate {
            getAllBooksRoute(booksRepository = booksRepository)
            getBookByIdRoute(booksRepository = booksRepository)
            createBookRoute(booksRepository = booksRepository)
            updateBookRoute(booksRepository = booksRepository)
            deleteBookRoute(booksRepository = booksRepository)
        }
    }
}

private fun Route.getAllBooksRoute(
    booksRepository: BooksRepository
) {
    get {
        val query = call.queryParameters["q"]
        val books = booksRepository.getBooks(query)
        call.respond(HttpStatusCode.OK, books)
    }
}

private fun Route.getBookByIdRoute(
    booksRepository: BooksRepository
) {
    get("/{id}") {
        val bookId = call.parameters["id"]!!.toInt()
        val book = booksRepository.getBookById(bookId)
        book
            ?.let { call.respond(HttpStatusCode.OK, book) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
}

private fun Route.createBookRoute(
    booksRepository: BooksRepository
) {
    post {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()
        val book = call.receive<BookDto>()
        try {
            val createdBook = booksRepository.insertBook(
                title = book.title,
                author = book.author,
                isbn = book.isbn,
                quantity = book.quantity
            )
            call.respond(HttpStatusCode.Created, createdBook)
        } catch (ex: ExposedSQLException) {
            throw ISBNMustBeUniqueException()
        }
    }
}

private fun Route.updateBookRoute(
    booksRepository: BooksRepository
) {
    put("/{id}") {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val bookId = call.parameters["id"]!!.toInt()
        val book = call.receive<BookDto>()
        try {
            val updatedBook = booksRepository.updateBook(
                id = bookId,
                title = book.title,
                author = book.author,
                isbn = book.isbn,
                quantity = book.quantity
            )
            updatedBook
                ?.let { call.respond(HttpStatusCode.OK, it) }
                ?: call.respond(HttpStatusCode.NotFound)
        } catch (ex: ExposedSQLException) {
            throw ISBNMustBeUniqueException()
        }
    }
}

private fun Route.deleteBookRoute(
    booksRepository: BooksRepository
) {
    delete("/{id}") {
        if (!isUserLibrarian()) throw UserIsNotLibrarianException()

        val bookId = call.parameters["id"]!!.toInt()
        val deleted = booksRepository.deleteBook(bookId)
        if (deleted) call.respond(HttpStatusCode.NoContent)
        else call.respond(HttpStatusCode.NotFound)
    }
}

