package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiLens
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.domain.User
import ru.yarsu.web.models.NotInSystemVM
import ru.yarsu.web.models.ProfileVM
import ru.yarsu.web.templates.ContextAwareViewRender

class ProfileHandler(val userLens: BiDiLens<Request, User?>, val specialistsStorage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val user = userLens(request)
        val person = specialistsStorage.getPersonByUserId(user!!.id)
            ?: return Response(Status.OK).with(htmlViewWithContext(request) of NotInSystemVM())
        return Response(Status.OK).with(htmlViewWithContext(request) of ProfileVM(person))
    }
}