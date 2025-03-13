package com.ajuliaoo.ibtlibrary.exceptions

class InvalidRoleException(override val message: String = "Forneça um cargo válido") : Exception(message)