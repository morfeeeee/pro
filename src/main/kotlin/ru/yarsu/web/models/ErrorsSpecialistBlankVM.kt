package ru.yarsu.web.models

import org.http4k.template.ViewModel


class ErrorsSpecialistBlankVM(val name: String?, val surname: String?, val patronymic: String?,
                              val phoneNumber: String?, val education1: String?, val education2: String?, val vkId: String,
                              val telegramId: String, val facebookId: String,
                              val listProfessions: List<String>, val errors: List<String>, val category: String?, val license: Boolean): ViewModel