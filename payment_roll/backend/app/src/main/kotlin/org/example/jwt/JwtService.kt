package org.example.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import org.example.AuthenticationException

const val SECRET_KEY = "WBMcQY7piOdrn0v9vzO1NVBomDRsOY4L4ky6L/wgLJB4c9STraa/H3SOnXZmm6nN"

class JwtService {
    private val algorithm = Algorithm.HMAC256(SECRET_KEY)
    private val issuer = "identity_provider"
    private val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    private fun validateAndDecode(token: String): DecodedJWT {
        return try {
            val jwt = verifier.verify(token)
            jwt
        } catch (_: TokenExpiredException) {
            throw AuthenticationException.ExpiredToken()
        } catch (_: AlgorithmMismatchException) {
            throw AuthenticationException.InvalidToken("Algoritmo diferente")
        } catch (_: SignatureVerificationException) {
            throw AuthenticationException.InvalidToken("Assinatura inválida")
        } catch (_: JWTDecodeException) {
            throw AuthenticationException.InvalidToken("Token mal formado")
        } catch (_: JWTVerificationException) {
            throw AuthenticationException.InvalidToken("Erro na verificação")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun getSubjectIfValid(token: String): String? {
        return validateAndDecode(token).subject
    }

    fun isValid(token: String): Boolean {
        return try {
            validateAndDecode(token)
            true
        } catch (_: AuthenticationException) {
            false
        }
    }
}