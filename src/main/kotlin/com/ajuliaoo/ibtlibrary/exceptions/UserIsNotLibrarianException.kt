package com.ajuliaoo.ibtlibrary.exceptions

class UserIsNotLibrarianException(override val message: String = "Você não tem permissões de bibliotecário") : Exception(message)
