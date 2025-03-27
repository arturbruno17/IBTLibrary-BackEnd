package com.ajuliaoo.ibtlibrary.exceptions

class InvalidLoanTypeException(
    override val message: String = "Forneça valores válidos para tipo de empréstimo"
) : Exception()