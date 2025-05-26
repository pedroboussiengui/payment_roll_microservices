package org.example.infra

import org.example.domain.User
import java.util.UUID

interface UserRepository {
    fun save(user: User)
    fun findById(id: UUID): User?
    fun findByUsername(username: String): User?
}

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<UUID, User>()

    override fun save(user: User) {
        users[user.userId] = user
    }

    override fun findById(id: UUID): User? {
        return users[id]
    }

    override fun findByUsername(username: String): User? {
        return users.values.find { it.username == username }
    }
}