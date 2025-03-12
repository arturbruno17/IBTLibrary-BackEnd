package com.ajuliaoo.ibtlibrary

import com.ajuliaoo.ibtlibrary.routing.auth.login.request.validateLoginDto
import com.ajuliaoo.ibtlibrary.routing.auth.register.request.validateRegisterDto
import com.ajuliaoo.ibtlibrary.routing.books.update.request.validateUpdateBookDto
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validateRegisterDto()
        validateLoginDto()
        validateUpdateBookDto()
    }
}
