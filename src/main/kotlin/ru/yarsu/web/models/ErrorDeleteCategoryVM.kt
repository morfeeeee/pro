package ru.yarsu.web.models

import org.http4k.template.ViewModel


class ErrorDeleteCategoryVM(val listCategory: List<String>, val message: String, val selectedCategory: String): ViewModel