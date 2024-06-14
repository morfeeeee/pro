package ru.yarsu.web.handlers

import org.http4k.core.*
import ru.yarsu.web.models.NewCategoryVM
import ru.yarsu.web.templates.ContextAwareViewRender

class NewCategoryHandler(val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlViewWithContext(request) of NewCategoryVM())
    }
}