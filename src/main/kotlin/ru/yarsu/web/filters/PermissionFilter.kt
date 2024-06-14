package ru.yarsu.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.lens.RequestContextLens
import ru.yarsu.domain.Permissions
import ru.yarsu.domain.User
import ru.yarsu.web.templates.ContextAwareViewRender

class PermissionFilter(
    val canUse: (Permissions) -> Boolean,
    val userLens: RequestContextLens<User?>,
    val htmlViewWithContext: ContextAwareViewRender
): Filter {
    override fun invoke(next: HttpHandler): HttpHandler{
        return PermissionHandler(next, canUse, userLens, htmlViewWithContext)
    }
}