package com.ajuliaoo.ibtlibrary.routing.books.update.request

import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookDto(
    val isbn: String,
    val title: String,
    val author: String?,
    val quantity: Int
)

fun RequestValidationConfig.validateUpdateBookDto() {
    validate<UpdateBookDto> { updateBookDto ->
        if (updateBookDto.quantity < 0) {
            ValidationResult.Invalid(listOf("A quantidade deve ser maior ou igual a 0"))
        }
        ValidationResult.Valid
    }
}