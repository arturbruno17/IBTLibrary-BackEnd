package com.ajuliaoo.ibtlibrary.security.token

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ):  Pair<String, String>
}