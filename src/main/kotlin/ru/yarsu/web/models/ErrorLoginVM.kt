package ru.yarsu.web.models

import org.http4k.template.ViewModel


class ErrorLoginVM(val login: String, val message: String): ViewModel
