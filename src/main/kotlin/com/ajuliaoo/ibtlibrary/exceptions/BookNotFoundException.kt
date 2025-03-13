package com.ajuliaoo.ibtlibrary.exceptions

class BookNotFoundException(override val message: String = "Livro não encontrado na nossa base de dados") : Exception(message)