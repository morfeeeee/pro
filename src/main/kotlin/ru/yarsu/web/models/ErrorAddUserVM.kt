package ru.yarsu.web.models

import org.http4k.template.ViewModel


class ErrorAddUserVM(val login: String, val message: String): ViewModel