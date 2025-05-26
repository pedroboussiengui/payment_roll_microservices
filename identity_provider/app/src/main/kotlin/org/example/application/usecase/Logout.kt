package org.example.application.usecase

import org.example.application.service.JWTService
import org.example.infra.redis.InMemoryDao
import org.example.infra.redis.Session

class Logout(
    private val inMemoryDao: InMemoryDao<Session>
) {
    fun execute(refreshToken: String) {
        val jwtService = JWTService()
        val sessionId = jwtService.getSessionId(refreshToken)
        sessionId?.let {
            println("Session found with id: $sessionId, deleting")
            inMemoryDao.delete(sessionId)
        }
    }
}