package ru.yarsu.web.filters

import org.http4k.core.HttpHandler
import org.http4k.core.Filter
import ru.yarsu.web.templates.ContextAwareViewRender


class NotFoundFilter(val htmlViewWithContext: ContextAwareViewRender): Filter{
    override fun invoke(next: HttpHandler): HttpHandler{
        return NotFoundHandler(next, htmlViewWithContext)
    }
}

