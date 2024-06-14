package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.EditAdvertisingVM
import ru.yarsu.web.templates.ContextAwareViewRender

class EditAdvertisingHandler(val specialistsStorage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        val person = specialistsStorage.getPersonById(id)
        return Response(Status.OK).with(htmlViewWithContext(request) of EditAdvertisingVM(person!!, specialistsStorage.getListProfession()))
    }
}