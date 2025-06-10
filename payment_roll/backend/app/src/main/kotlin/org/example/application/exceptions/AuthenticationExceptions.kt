package org.example.application.exceptions

/**
 * token ausente -          MissingToken
 * token expirado -         ExpiredToken
 * algoritmo diferente -    InvalidToken
 * assinatura inválida -    InvalidToken
 * token mal formado -      InvalidToken
 * erro na verificação -    InvalidToken
 * erro genérico -          Exception
 */

sealed class AuthenticationException(
    override val message: String
) : RuntimeException(message) {

    class MissingToken : RuntimeException("Access token is missing")

    class ExpiredToken: RuntimeException("Access token is expired")

    class InvalidToken(reason: String) : RuntimeException(reason)
}