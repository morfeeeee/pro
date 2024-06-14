package ru.yarsu.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import kotlin.concurrent.thread

class DatabaseConfig(
    val host: String,
    val port: Int,
    val database: String
) {
    companion object {
        val hostLens = EnvironmentKey.nonEmptyString().required("db.host", "Database host")
        val portLens = EnvironmentKey.int().required("db.port", "Database port")
        val databaseLens = EnvironmentKey.nonEmptyString().required("db.user", "Database name")

        fun from(environment: Environment): DatabaseConfig {
            return DatabaseConfig(
                host = hostLens(environment),
                port = portLens(environment),
                database = databaseLens(environment)
            )
        }
        val defaultEnvironmentDatabase = Environment.defaults(
            hostLens of "localhost:",
            portLens of 9001,
            databaseLens of "users"
        )
    }






    fun toJdbcUrl(): String {
        return "jdbc:h2:tcp://$host:$port/$database"
    }
}