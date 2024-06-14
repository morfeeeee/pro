package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Profile


class PersonEditVM(val person: Profile, val vkId: String, val telegramId: String,
                   val facebookId: String, val listProfessions: List<String>): ViewModel
