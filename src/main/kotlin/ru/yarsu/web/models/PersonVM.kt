package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Profile


class PersonVM (val person: Profile,
                val profession: String) : ViewModel
