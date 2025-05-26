package org.example.infra.redis

import redis.clients.jedis.Jedis

object RedisConnection {
    private val host = "localhost"
    private val port = 6379

    val jedis: Jedis by lazy {
        Jedis(host, port)
    }
}