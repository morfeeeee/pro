package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.routing.path
import ru.yarsu.domain.UserStorage
import ru.yarsu.web.templates.ContextAwareViewRender


class PostEditUserHandler(
    val userStorage: UserStorage,
    val htmlViewWithContext: ContextAwareViewRender
): HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val role = form.findSingle("role")
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        val user = userStorage.getUserById(id)
        userStorage.editUser(user!!, role!!)

        return Response(Status.FOUND).header("Location", "/users")
    }
}