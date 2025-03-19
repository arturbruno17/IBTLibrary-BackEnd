package com.ajuliaoo.ibtlibrary.exceptions

class AllInStockWereLoaned(override val message: String = "Todos os exemplares desse livro estão emprestados") : Exception(message)