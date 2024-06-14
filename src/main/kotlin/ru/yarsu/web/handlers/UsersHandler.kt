package ru.yarsu.web.handlers

import org.http4k.core.*
import ru.yarsu.domain.UserStorage
import ru.yarsu.web.models.UsersVM
import ru.yarsu.web.templates.ContextAwareViewRender

class UsersHandler(val usersStorage: UserStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val viewModel = UsersVM(usersStorage.getListUsers())
        return Response(Status.OK).with(htmlViewWithContext(request) of viewModel)
    }
}