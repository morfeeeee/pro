package ru.yarsu.domain

import java.time.LocalDateTime

class Profile(
    val name: String,
    val surname: String,
    val patronymic: String,
    val phoneNumber: String,
    val education: List<String>,
    val idSocialNetwork: List<String>,
    val dateAdd: LocalDateTime,
    val license: Boolean,
    val id: Int,
    val category: Category,
    val personId: Int,
)