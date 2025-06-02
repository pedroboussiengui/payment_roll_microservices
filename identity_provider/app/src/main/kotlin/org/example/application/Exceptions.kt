package org.example.application


class SessionExpiredException(
    override val message: String
) : RuntimeException()

sealed class AuthenticationException(
    override val message: String
) : RuntimeException(message) {
    class MissingToken : RuntimeException("Access token is missing")

    class CredentialsFailed : RuntimeException("Password or username are incorrect")

    class ExpiredToken: RuntimeException("Access token is expired")

    class InvalidToken(reason: String) : RuntimeException(reason)
}