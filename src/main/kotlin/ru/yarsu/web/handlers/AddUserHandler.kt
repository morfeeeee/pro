package ru.yarsu.web.handlers

import org.http4k.core.*
import ru.yarsu.web.models.AddUserVM
import ru.yarsu.web.templates.ContextAwareViewRender

class AddUserHandler(val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlViewWithContext(request) of AddUserVM())
    }
}