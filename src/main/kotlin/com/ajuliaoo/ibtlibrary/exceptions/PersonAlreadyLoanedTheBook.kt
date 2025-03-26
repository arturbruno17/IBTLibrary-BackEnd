package com.ajuliaoo.ibtlibrary.exceptions

class PersonAlreadyLoanedTheBook(
    private val bookName: String,
    override val message: String = "O usuário já tem o livro \"$bookName\" com empréstimo ativo"
) : Exception(message)