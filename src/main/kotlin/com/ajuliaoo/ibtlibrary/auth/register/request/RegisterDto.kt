package com.ajuliaoo.ibtlibrary.auth.register.request

import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    val name: String,
    val email: String,
    val password: String,
)


fun RequestValidationConfig.validateRegisterDto() {
    validate<RegisterDto> { registerDto ->
        val reasons = mutableListOf<String>()

        val emailRegex = Regex("^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$")
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[!#@\$%&.])(?=.*[0-9])(?=.*[a-z]).{8,}\$")

        if (registerDto.name.isBlank()) {
            reasons.add("O nome não pode ser vaio")
        }

        if (!emailRegex.matches(registerDto.email)) {
            reasons.add("Um e-mail válido deve ser fornecido")
        }

        if (!passwordRegex.matches(registerDto.password)) {
            reasons.add("A senha deve conter, no mínimo, 8 caracteres, sendo eles uma letra maiúscula, uma letra minúscula, um dígito e um caractere especial")
        }

        if (reasons.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(reasons)
        }
    }
}