package org.example.application.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.example.domain.User
import java.time.Instant

const val SECRET_KEY = "WBMcQY7piOdrn0v9vzO1NVBomDRsOY4L4ky6L/wgLJB4c9STraa/H3SOnXZmm6nN"

class JWTService {

    private val algorithm = Algorithm.HMAC256(SECRET_KEY)
    private val issuer = "identity_provider"
    private val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generatePartialToken(user: User): String {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(user.userId.toString())
            .withClaim("type", "partial")
            .withClaim("name", user.username)
            .withExpiresAt(Instant.now().plusSeconds(60 * 5))
            .sign(algorithm)
    }

    fun generateAccessToken(user: User, contractId: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(user.userId.toString())
            .withClaim("type", "access_token")
            .withClaim("name", user.username)
            .withClaim("contract_id", contractId)
            .withExpiresAt(Instant.now().plusSeconds(60 * 5))
            .sign(algorithm)
    }

    fun verify(token: String): Boolean {
        return try {
            verifier.verify(token)
            true
        } catch (ex: JWTVerificationException) {
            false
        }
    }

    fun getSubject(token: String): String {
        return verifier.verify(token).subject
    }
}