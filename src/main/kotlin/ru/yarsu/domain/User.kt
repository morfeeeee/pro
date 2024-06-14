package ru.yarsu.domain

import java.time.LocalDateTime

class User (
    val login: String,
    val password: Password,
    val dateAdd: LocalDateTime,
    val id: Int,
    val role: String
)