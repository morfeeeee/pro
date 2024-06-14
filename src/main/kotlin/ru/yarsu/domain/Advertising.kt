package ru.yarsu.domain

import java.time.LocalDateTime

class Advertising (
    val category: Category,
    val license: Boolean,
    val dateAdd: LocalDateTime,
    val id: Int,
)
