package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Profile

class SpecialistsVM (val listSpecialists: List<Profile>, val tranlater: Map<String, String>,
                     val listPages: List<String>, val profession: String,
                     val url: String, val emptyOrNot: Int, val countEducation: String, val license: Boolean?) : ViewModel
