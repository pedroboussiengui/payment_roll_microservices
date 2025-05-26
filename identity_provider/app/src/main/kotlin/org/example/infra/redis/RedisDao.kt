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
}

@Serializable
data class Session(
    val sessionId: String,
    val userId: String,
    var refreshTokenHash: String,
    var refreshedAt: String? = null,
    val expiresAt: String
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
        jedis.set(key, json.encodeToString(value))
    }

    override fun get(key: String): Session? {
        val sessionEncoded = jedis.get(key)?: return null
        return json.decodeFromString(sessionEncoded)
    }

    override fun delete(key: String) {
        jedis.del(key)
    }
}