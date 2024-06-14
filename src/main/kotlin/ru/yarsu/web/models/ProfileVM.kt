package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Profile


class ProfileVM(val person: Profile): ViewModel
