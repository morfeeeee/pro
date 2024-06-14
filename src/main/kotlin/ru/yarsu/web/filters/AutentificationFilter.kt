package ru.yarsu.web.filters

import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextLens
import ru.yarsu.domain.JwtTools
import ru.yarsu.domain.User
import ru.yarsu.domain.UserStorage

class AutentificationFilter(
    private val jwtTools: JwtTools,
    private val userLens: RequestContextLens<User?>,
    private val cookieLens: BiDiLens<Request, Cookie?>,
    private val userStorage: UserStorage
) : Filter {

    override fun invoke(nextHandler: HttpHandler): HttpHandler {
        return { request: Request ->
            processRequest(request, nextHandler)
        }
    }

    private fun processRequest(request: Request, nextHandler: HttpHandler): Response {
        val cookie = cookieLens(request)
        if (cookie != null) {
            val token = cookie.value
            val userId = jwtTools.checkJwtToken(token)
            if (userId != null) {
                val user = userStorage.getUserById(userId)
                if (user != null) {
                    val updatedRequest = userLens(user, request)
                    return nextHandler(updatedRequest)
                }
            }
        }
        return nextHandler(request)
    }
}
