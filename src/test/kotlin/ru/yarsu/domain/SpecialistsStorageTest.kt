package ru.yarsu.domain

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.File

class SpecialistsStorageTest : FunSpec({
    val objectMapper = jacksonObjectMapper()
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
    objectMapper.registerModule(JavaTimeModule())
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    val readerSpecialists = File("Specialists.json").readText()
    val listSpecialists: List<Profile> = objectMapper.readValue(readerSpecialists)
    val specialistsStorage = SpecialistsStorage(listSpecialists)
    test("specialistsByPage(page = 1) return list who has size = 6") {
        specialistsStorage.specialistsByPage(1).size.shouldBe(6)
    }
})