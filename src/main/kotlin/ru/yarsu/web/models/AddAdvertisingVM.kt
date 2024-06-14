package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Profile


class AddAdvertisingVM(val person: Profile, val listCategory: List<String>): ViewModel
