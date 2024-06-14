package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.yarsu.domain.UserStorage
import ru.yarsu.web.models.EditUserVM
import ru.yarsu.web.templates.ContextAwareViewRender


class EditUserHandler(val userStorage: UserStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        val user = userStorage.getUserById(id)
        return Response(Status.OK).with(htmlViewWithContext(request) of EditUserVM(user!!))
    }
}