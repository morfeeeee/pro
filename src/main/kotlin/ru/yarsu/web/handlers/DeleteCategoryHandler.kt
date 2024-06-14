package ru.yarsu.web.handlers

import org.http4k.core.*
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.DeleteCategoryVM
import ru.yarsu.web.templates.ContextAwareViewRender

class DeleteCategoryHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlViewWithContext(request) of DeleteCategoryVM(storage.getListProfession()))
    }
}