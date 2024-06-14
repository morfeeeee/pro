package ru.yarsu.web.models

import org.http4k.template.ViewModel


class ErrorCategoryVM(val category: String, val message: String): ViewModel
