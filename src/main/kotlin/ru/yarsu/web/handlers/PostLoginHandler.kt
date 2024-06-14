package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import ru.yarsu.config.getAppConfig
import ru.yarsu.domain.*
import ru.yarsu.web.models.ErrorLoginVM
import ru.yarsu.web.templates.ContextAwareViewRender


class PostLoginHandler(val userStorage: UserStorage,
                       val saltStorage: SaltStorage,
                       val specialistsStorage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender
): HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val login = form.findSingle("login")
        val password = form.findSingle("password")
        if(login == null || login.replace(" ", "") == "")
            return Response(Status.OK).with(htmlViewWithContext(request) of ErrorLoginVM("", "Вы не ввели логин"))
        if(password == null || password.replace(" ", "") == "")
            return Response(Status.OK).with(htmlViewWithContext(request) of ErrorLoginVM(login, "Вы не ввели пароль"))
        val userMap = userStorage.identificationLogin(login)
        if(userMap.keys.contains(false)){
            return Response(Status.OK).with(htmlViewWithContext(request) of ErrorLoginVM(login, "Неверный логин или пароль"))
        }
        else{
            for(user1 in userMap.values){
                val salt = saltStorage.getSalt(user1.id)?.password
                if(user1.password.password != userStorage.identificationUser(password, salt!!)){
                    return Response(Status.OK).with(htmlViewWithContext(request) of ErrorLoginVM(login, "Неверный логин или пароль"))
                }
            }
        }

        val configuration = getAppConfig()
        val jwtTools = JwtTools(configuration.jwtConfig.secret, configuration.jwtConfig.organization, configuration.jwtConfig.tokenTimeLife)
        val token = jwtTools.createJwtToken(userMap[true]!!.id)
        var location = "/"
        if(specialistsStorage.getPersonByUserId(userMap[true]!!.id) == null && userMap[true]!!.role != "admin" && userMap[true]!!.role != "moderator") location = "/addPerson"
        return Response(Status.FOUND).header("Location", location).cookie(Cookie("auth", token.toString()))
    }
}