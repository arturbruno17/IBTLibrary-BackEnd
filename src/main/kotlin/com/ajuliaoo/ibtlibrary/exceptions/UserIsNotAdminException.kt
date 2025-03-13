package com.ajuliaoo.ibtlibrary.exceptions

class UserIsNotAdminException(override val message: String = "Você não tem permissões de administrador") : Exception(message)
