package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Profile

class MyAdvertisingVM(val listAdvertising: List<Profile>, val translater: Map<String, String>): ViewModel
