package com.ajuliaoo.ibtlibrary.security.token

data class TokenConfig(
    val secret: String,
    val expiresAt: Long,
    val subject: String
)
