package org.example.infra.redis

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

interface InMemoryDao<T> {
    fun save(key: String, value: T, expiresAt: Long)
    fun save(key: String, value: T)
    fun get(key: String): T?
    fun delete(key: String)
    fun getWithTTL(key: String): SessionWithTTL?
}

@Serializable
data class Session(
    val sessionId: String,
    val userId: String,
    var refreshTokenHash: String,
    var refreshedAt: String? = null,
    val expiresAt: String
)

@Serializable
data class SessionWithTTL(
    val sessionId: String,
    val userId: String,
    var refreshTokenHash: String,
    var refreshedAt: String? = null,
    val expiresAt: String,
    var ttl: Long
)

class RedisInMemoryDao(
    redisConnection: RedisConnection
) : InMemoryDao<Session> {

    private val jedis = redisConnection.jedis
    private val json = Json

    override fun save(key: String, value: Session, expiresAt: Long) {
        val sessionEncoded = json.encodeToString(value)
        val ttlInSeconds = expiresAt - Instant.now().epochSecond
        jedis.setex(key, ttlInSeconds, sessionEncoded)
    }

    override fun save(key: String, value: Session) {
        val ttl = jedis.ttl(key)
        if (ttl > 0) {
            jedis.setex(key, ttl, json.encodeToString(value))
        }
    }

    override fun get(key: String): Session? {
        val sessionEncoded = jedis.get(key)?: return null
        return json.decodeFromString(sessionEncoded)
    }

    override fun delete(key: String) {
        jedis.del(key)
    }

    override fun getWithTTL(key: String): SessionWithTTL? {
        val sessionEncoded = jedis.get(key)?: return null
        val ttl = jedis.ttl(key)
        val session = json.decodeFromString<Session>(sessionEncoded)
        val sessionWithTTL = SessionWithTTL(
            sessionId = session.sessionId,
            userId = session.userId,
            refreshTokenHash = session.refreshTokenHash,
            refreshedAt = session.refreshedAt,
            expiresAt = session.expiresAt,
            ttl = ttl
        )
        return sessionWithTTL
    }
}