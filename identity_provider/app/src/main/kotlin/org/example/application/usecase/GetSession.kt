package org.example.application.usecase

import org.example.infra.redis.InMemoryDao
import org.example.infra.redis.Session
import org.example.infra.redis.SessionWithTTL

class GetSession(
    private val inMemoryDao: InMemoryDao<Session>
) {
    fun execute(sessionId: String): SessionWithTTL? {
        return inMemoryDao.getWithTTL(sessionId)
    }
}