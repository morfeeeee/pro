package ru.yarsu.web.filters

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import ru.yarsu.domain.Permissions
import ru.yarsu.domain.PermissionsStorage
import ru.yarsu.domain.User
import ru.yarsu.web.models.NotInSystemVM
import ru.yarsu.web.templates.ContextAwareViewRender

class PermissionHandler(val next: HttpHandler,
                        val canUse: (Permissions) -> Boolean, val userLens: RequestContextLens<User?>, val htmlViewWithContext: ContextAwareViewRender
): HttpHandler{
    override fun invoke(request: Request): Response {
        val response = next(request)
        val user = userLens(request) ?: return response.with(htmlViewWithContext(request) of NotInSystemVM())
        val userPermissions = PermissionsStorage().getPermisssions(user.role)
        if(!canUse(userPermissions)) return response.with(htmlViewWithContext(request) of NotInSystemVM())
        return response
    }
}