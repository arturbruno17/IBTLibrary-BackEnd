package com.ajuliaoo.ibtlibrary.auth.login.request

import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val email: String,
    val password: String,
)

fun RequestValidationConfig.validateLoginDto() {
    validate<LoginDto> { loginDto ->
        val reasons = mutableListOf<String>()

        val emailRegex = Regex("^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$")

        if (!emailRegex.matches(loginDto.email)) {
            reasons.add("Um e-mail válido deve ser fornecido")
        }

        if (reasons.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(reasons)
        }
    }
}
