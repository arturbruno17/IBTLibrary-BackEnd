package com.ajuliaoo.ibtlibrary.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.Instant
import kotlin.time.Duration.Companion.days

class JwtTokenService: TokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): Pair<String, String> {
        var accessToken = JWT.create()
            .withSubject(config.subject)
            .withExpiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + config.expiresAt))
        claims.forEach { claim ->
            accessToken = accessToken.withClaim(claim.name, claim.value)
        }

        var refreshToken = JWT.create()
            .withSubject(config.subject)
            .withExpiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + 1.days.inWholeMilliseconds))
        claims.forEach {
            refreshToken = refreshToken.withClaim(it.name, it.value)
        }
        return Pair(
            first = accessToken.sign(Algorithm.HMAC256(config.secret)),
            second = refreshToken.sign(Algorithm.HMAC256(config.secret))
        )
    }
}