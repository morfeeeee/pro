package ru.yarsu.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.cloudnative.env.Secret
import org.http4k.lens.long
import org.http4k.lens.secret
import org.http4k.lens.string

data class JwtConfig(
    val secret: String,
    val organization: String,
    val tokenTimeLife: Long
) {
    companion object {
        val secretLens = EnvironmentKey.string().required("jwt.secret", "JWT secret")
        val organizationLens = EnvironmentKey.required("jwt.organization", "JWT organization")
        val tokenTimeLifeLens = EnvironmentKey.long().required("jwt.tokenValidity", "JWT token validity in ms")
        fun createJwtConfig(environment: Environment): JwtConfig {
            return JwtConfig(
                secretLens(environment),
                organizationLens(environment),
                tokenTimeLifeLens(environment)
            )
        }
    }
}

