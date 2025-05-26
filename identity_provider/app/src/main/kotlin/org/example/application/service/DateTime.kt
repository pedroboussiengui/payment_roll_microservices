package org.example.application.service

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun getTimestamp(expireLong: Long): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(expireLong),
        ZoneId.systemDefault()
    )
}