package ru.yarsu.domain

class Person (
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String,
    val phoneNumber: String,
    val education: List<String>,
    val idSocialNetwork: List<String>,
    val listAdvertising: List<Advertising>,

)
