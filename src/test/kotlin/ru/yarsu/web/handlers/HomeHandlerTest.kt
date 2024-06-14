package ru.yarsu.web.handlers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.kotest.haveStatus
import ru.yarsu.domain.Profile
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.templateRend
import java.io.File

class HomeHandlerTest : FunSpec({
    val objectMapper = jacksonObjectMapper()
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
    objectMapper.registerModule(JavaTimeModule())
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    val readerSpecialists = File("Specialists.json").readText()
    val listSpecialists: List<Profile> = objectMapper.readValue(readerSpecialists)
    val specialistsStorage = SpecialistsStorage(listSpecialists)
    val format = templateRend(true)
    test("Should respond with non-empty body and good status") {
        val request = Request(Method.GET, "/")
        val handler = HomeHandler(format, specialistsStorage)
        val response = handler(request)
        response should haveStatus(Status.OK)
        response.body.length shouldNotBe null
        response.body.length?.shouldBeGreaterThan(1L)
    }
})