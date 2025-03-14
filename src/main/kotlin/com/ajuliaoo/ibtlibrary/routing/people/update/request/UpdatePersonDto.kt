package com.ajuliaoo.ibtlibrary.routing.people.update.request

import com.ajuliaoo.ibtlibrary.models.Role
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePersonDto(
    val name: String,
    val email: String,
    val role: Role? = null
)

fun RequestValidationConfig.validateUpdatePersonDto() {
    val reasons = mutableListOf<String>()

    validate<UpdatePersonDto> { updatePersonDto ->
        val emailRegex =
            Regex("^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$")

        if (updatePersonDto.name.isBlank()) {
            reasons.add("O nome não pode ser vazio")
        }

        if (!emailRegex.matches(updatePersonDto.email)) {
            reasons.add("Um e-mail válido deve ser fornecido")
        }

        if (updatePersonDto.role != null && updatePersonDto.role !in listOf(Role.READER, Role.LIBRARIAN)) {
            reasons.add("Apenas cargos de leitor e bibliotecário são aceitos. Entre em contato com o administrador do sistema")
        }

        if (reasons.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(reasons)
        }
    }
}