package com.ajuliaoo.ibtlibrary.exceptions

class ISBNMustBeUniqueException(override val message: String = "Número ISBN deve ser único") : Exception(message)