package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.auth.login.request.validateLoginDto
import com.ajuliaoo.ibtlibrary.auth.register.request.validateRegisterDto
import com.ajuliaoo.ibtlibrary.books.update.request.validateUpdateBookDto
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validateRegisterDto()
        validateLoginDto()
        validateUpdateBookDto()
    }
}
