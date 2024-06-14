package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.DeletePersonVM
import ru.yarsu.web.templates.ContextAwareViewRender

class GetDeletePersonHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        val person = storage.getPersonById(id)
        return Response(Status.OK).with(htmlViewWithContext(request) of DeletePersonVM(person, person?.let { storage.getTranslateProfessionToEnglish(it.category.name) }))
    }
}