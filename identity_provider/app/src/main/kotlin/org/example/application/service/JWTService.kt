package org.example.application.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import org.example.application.AuthenticationException
import org.example.domain.User
import java.time.Instant
import java.util.Date

/**
 * Aqui estamos a utilizar um chave unica, pois é JWS, a assinatura é de chave simétrica.
 * Essa chave é usada tanto para assinar ‘tokens’ como para verificar a assinatura.
 * Ele terá que ser exportado para a aplicação de negócio que usa os ‘tokens’, porque
 * os ‘tokens’ também precisam ser validados a cada requisição.
 * Logo é melhor usar uma par de chaves públicas e privadas. A privada apenas assina os 'tokens'
 * e pode ser mantida no IDP enquanto a pública pode ser espalhada, pois ela apenas verifica
 * a autenticidade dos 'tokens' que chegam
 */
const val SECRET_KEY = "WBMcQY7piOdrn0v9vzO1NVBomDRsOY4L4ky6L/wgLJB4c9STraa/H3SOnXZmm6nN"

const val SECRET_KEY_REFRESH = "LMXeQAW1QpcZNiUl+J4NoMIFY0G07HWdYpmREx8H9W/AaJiWXUX/ow96efgzIByp"

class JWTService {

    // HMAC256 - symmetric key
    private val algorithm = Algorithm.HMAC256(SECRET_KEY) // RFC 7515: JSON Web Signature (JWS)
    private val issuer = "identity_provider"
    private val partialTokenExpiration: Long = 60 * 5 // 5 mim
    private val accessTokenExpiration: Long = 40 // 5 min
    private val refreshTokenExpiration: Long = 120 // 30 min
    private val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generatePartialToken(user: User): String {
        return JWT.create() // RFC 7519: JSON Web Token (JWT)
            .withIssuer(issuer)
            // .issuedAt
            .withSubject(user.userId.toString())
            .withClaim("type", "partial_token")
            .withClaim("name", user.username)
            .withExpiresAt(Instant.now().plusSeconds(partialTokenExpiration))
            .sign(algorithm)
    }

    fun generateAccessToken(user: User, contractId: String, instant: Instant): String {
        return JWT.create()
            .withIssuer(issuer)
            // issuedAt
            .withSubject(user.userId.toString())
            .withClaim("type", "access_token")
            .withClaim("name", user.username)
            .withClaim("contract_id", contractId)
            .withExpiresAt(instant.plusSeconds(accessTokenExpiration))
            .sign(algorithm)
    }

    /**
     * todo: gerar refresh token
     * - algoritmo key diferente do access token
     * - armazenar em memória para poder revogar
     * - expiração maior
     */
    fun generateRefreshToken(user: User, sessionId: String, contractId: String, instant: Instant): String {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(user.userId.toString())
            .withClaim("type", "refresh_token")
            .withClaim("session_id", sessionId)
            .withClaim("contract_id", contractId)
            .withExpiresAt(instant.plusSeconds(refreshTokenExpiration))
            .sign(algorithm)
    }

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

    fun isValid(token: String): Boolean {
        return try {
            validateAndDecode(token)
            true
        } catch (_: AuthenticationException) {
            false
        }
    }

    fun getSubjectIfValid(token: String): String {
        return validateAndDecode(token).subject
    }

    fun getExpireAt(token: String): Date {
        return verifier.verify(token).expiresAt
    }

    fun getContractId(token: String): String? {
        return verifier.verify(token).claims["contract_id"]?.asString()
    }

    fun getExpiresAtAsEpochMillis(token: String): Long {
        return verifier.verify(token).expiresAt.toInstant().epochSecond
    }

    fun getSessionId(refreshToken: String): String? {
        return verifier.verify(refreshToken).claims["session_id"]?.asString()
    }
}