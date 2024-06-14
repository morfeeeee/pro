package ru.yarsu.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*


class JwtTools(val secret: String, val organization: String, val tokenTimeLife: Long){
       fun createJwtToken(id: Int): String? {
           return try {
               val algorithm = Algorithm.HMAC512(secret);
               val token = JWT.create()
                   .withSubject(id.toString())
                   .withIssuer(organization)
                   .withExpiresAt(Date(Date().time + tokenTimeLife))
                   .sign(algorithm)
               token
           } catch (exception: JWTCreationException){
               null
           }
       }

    fun checkJwtToken(token: String): Int?{
        val algorithm = Algorithm.HMAC512(secret)
        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(organization)
            .build(); // Можно создать единожды для приложения
        return try {
            val decodedJWT: DecodedJWT = verifier.verify(token)
            decodedJWT.subject.toInt()
        } catch (exception: JWTVerificationException){
            null
            // Неправильная подпись или утверждения
        }
    }

}