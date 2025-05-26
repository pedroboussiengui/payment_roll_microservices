package org.example.infra.hash

import com.password4j.Password

interface PasswordHash {
    fun hash(password: String): String
    fun check(password: String, hash: String): Boolean
}

class BCryptPasswordHash : PasswordHash {

    override fun hash(password: String): String {
        val hashPassword = Password.hash(password)
            .addRandomSalt()
            .withArgon2()
        return hashPassword.result
    }

    override fun check(password: String, hash: String): Boolean {
        return Password.check(password, hash).withArgon2()
    }

}