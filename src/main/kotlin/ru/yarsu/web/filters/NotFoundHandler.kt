package ru.yarsu.web.filters

import org.http4k.core.*
import ru.yarsu.web.models.NotFoundVM
import ru.yarsu.web.templates.ContextAwareViewRender

class NotFoundHandler(val next: HttpHandler, val htmlViewWithContext: ContextAwareViewRender): HttpHandler{
    override fun invoke(request: Request): Response {
        val response = next(request)
        if(response.status == Status.NOT_FOUND){
            return response.with(htmlViewWithContext(request) of NotFoundVM())
        }
        return response
    }
}