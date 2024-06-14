package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Profile


class EditAdvertisingVM(val person: Profile, val listCategory: List<String>): ViewModel