package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import ru.yarsu.domain.*
import ru.yarsu.web.models.ErrorAddUserVM
import ru.yarsu.web.templates.ContextAwareViewRender
import java.security.MessageDigest
import java.time.LocalDateTime

class PostAddUserHandler(val usersStorage: UserStorage, val saltStorage: SaltStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val login = form.findSingle("login") ?: return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM("", "Введите логин"))
        if(login.replace(" ", "") == "") return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM("", "Введите логин"))
        val password1 = form.findSingle("password1")
            ?: return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Введите пароль"))
        if(password1.replace(" ", "") == "") return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Введите пароль"))
        val password2 = form.findSingle("password2")
            ?: return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Повторите введенный пароль"))
        if(password2.replace(" ", "") == "") return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Повторите введенный пароль"))
        if(login[0].isDigit()) return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Логин не может начинаться с цифры"))
        if(login.contains(" ")) return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Логин не может содержать пробелы"))
        if(password1.contains(" ")) return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Пароль не может содержать пробелы"))
        if(password1 != password2) return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Пароли не совпадают"))
        for(user1 in usersStorage.getListUsers()){
            if(user1.login == login) return Response(Status.OK).with(htmlViewWithContext(request) of ErrorAddUserVM(login, "Такой логин уже существует"))
        }

        var role = form.findSingle("role")
        if(role == null || role == "") role = "specialist"
        val password = Password(saltStorage.idCounter, password1)
        saltStorage.addPassword(password)

        val hexPassword = saltStorage.getSalt(password.id)?: return Response(Status.NOT_FOUND)
        val hexPasswordString = password1 + hexPassword.password
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(hexPasswordString.toByteArray())
        val user1 = User(login, Password(hexPassword.id, digest.joinToString("") { "%02x".format(it) }),
            LocalDateTime.now(), hexPassword.id, role)
        usersStorage.addUser(user1)

        return Response(Status.FOUND).header("Location", "/")
    }
}