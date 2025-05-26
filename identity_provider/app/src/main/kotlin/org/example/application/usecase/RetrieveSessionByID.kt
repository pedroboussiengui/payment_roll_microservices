package org.example.application.usecase

import org.example.infra.redis.InMemoryDao
import org.example.infra.redis.Session

class RetrieveSessionByID(
    private val inMemoryDao: InMemoryDao<Session>
) {
    fun execute(sessionId: String): Session {
        return inMemoryDao.get(sessionId)
            ?: throw Exception("Session not found")
    }
}