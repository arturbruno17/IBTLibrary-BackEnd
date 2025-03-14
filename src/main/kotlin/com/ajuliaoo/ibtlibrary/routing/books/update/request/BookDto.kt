package com.ajuliaoo.ibtlibrary.routing.books.update.request

import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val isbn: String,
    val title: String,
    val author: String?,
    val quantity: Int
)

fun RequestValidationConfig.validateUpdateBookDto() {
    validate<BookDto> { bookDto ->
        val reasons = mutableListOf<String>()

        if (bookDto.isbn.count() in 10..13) {
            reasons.add("Número ISBN deve estar preenchido e conter entre 10 a 13 dígitos")
        }

        if (bookDto.title.isBlank()) {
            reasons.add("O título não deve ser vazio")
        }

        if (bookDto.quantity < 0) {
            reasons.add("A quantidade deve ser maior ou igual a 0")
        }

        if (reasons.isNotEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(reasons)
        }
    }
}