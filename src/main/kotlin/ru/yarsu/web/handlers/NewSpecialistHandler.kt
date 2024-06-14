package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiLens
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.domain.User
import ru.yarsu.web.models.NewSpecialistVM
import ru.yarsu.web.models.NotInSystemVM
import ru.yarsu.web.templates.ContextAwareViewRender

class NewSpecialistHandler(val storage: SpecialistsStorage, val userLens: BiDiLens<Request, User?>, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        if(userLens(request) == null) return Response(Status.OK).with(htmlViewWithContext(request) of NotInSystemVM())
        val listProfessions = storage.getListProfession()
        return Response(Status.OK).with(htmlViewWithContext(request) of NewSpecialistVM(listProfessions))
    }
}