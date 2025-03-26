package com.ajuliaoo.ibtlibrary.exceptions

class UserNotFoundException(override val message: String = "Usuário não encontrado na nossa base de dados") : Exception(message)