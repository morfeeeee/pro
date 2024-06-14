package ru.yarsu.web.handlers

import org.http4k.core.*
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.HomePageVM
import ru.yarsu.web.templates.ContextAwareViewRender

class HomeHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {

    override fun invoke(request: Request): Response {
        val model = HomePageVM(storage.getTranslater())
        return Response(Status.OK).with(htmlViewWithContext(request) of model)
    }
}