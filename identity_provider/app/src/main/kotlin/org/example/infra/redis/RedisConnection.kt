package org.example.infra.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

object RedisConnection {
    private val host = "localhost"
    private val port = 6379

    val jedis: Jedis by lazy {
        val client = Jedis(host, port)
        try {
            client.ping()
        } catch (e: Exception) {
            throw IllegalStateException("Cannot connect to Redis: ${e.message}", e)
        }
        client
    }
}

//Uma abordagem mais resiliente
//
//    private val pool: JedisPool by lazy {
//        JedisPool(host, port)
//    }
//
//    fun <T> withConnection(block: (Jedis) -> T): T {
//        pool.resource.use { jedis ->
//            return block(jedis)
//        }
//    }
//
//RedisConnection.withConnection { jedis ->
//    jedis.set("key", "value")
//}