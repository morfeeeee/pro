package ru.yarsu.web.handlers

import org.http4k.core.*
import ru.yarsu.web.models.LoginVM
import ru.yarsu.web.templates.ContextAwareViewRender

class LoginHandler(val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlViewWithContext(request) of LoginVM())
    }
}