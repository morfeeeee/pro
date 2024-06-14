package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiLens
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.domain.User
import ru.yarsu.web.models.AddAdvertisingVM
import ru.yarsu.web.templates.ContextAwareViewRender


class AddAdvertisingHandler(val userLens: BiDiLens<Request, User?>, val specialistsStorage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val user = userLens(request)
        val person = specialistsStorage.getPersonByUserId(user!!.id)
            ?: return Response(Status.FOUND).header("Location", "/addPerson")
        return Response(Status.OK).with(htmlViewWithContext(request) of AddAdvertisingVM(person, specialistsStorage.getListProfession()))
    }
}
