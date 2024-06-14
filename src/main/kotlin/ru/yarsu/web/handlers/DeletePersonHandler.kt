package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.routing.path
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.templates.ContextAwareViewRender

class DeletePersonHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        val person = storage.getPersonById(id) ?: return Response(Status.NOT_FOUND)
        val form = request.form()
        val checkDel = form.findSingle("delete")

        if(checkDel != null){
            storage.deletePerson(person)
            return Response(Status.FOUND).header("Location", "/")
        }

        return Response(Status.FOUND).header("Location", "/profession${storage.getTranslateProfessionToEnglish(person.education[0])}/person${person.id}")
    }
}