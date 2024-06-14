package ru.yarsu.config

import org.http4k.cloudnative.env.Environment
import ru.yarsu.config.DatabaseConfig.Companion.defaultEnvironmentDatabase
import ru.yarsu.config.DatabaseConfig.Companion.from
import ru.yarsu.config.JwtConfig.Companion.createJwtConfig
import ru.yarsu.config.WebConfig.Companion.createWebConfig
import ru.yarsu.config.WebConfig.Companion.defaultEnv

data class AppConfig(
    val webConfig: WebConfig,
    val databaseConfig: DatabaseConfig,
    val jwtConfig: JwtConfig
)

val appEnv = Environment.fromResource("ru/yarsu/config/app.properties") overrides
        Environment.JVM_PROPERTIES overrides
        Environment.ENV overrides
        defaultEnv overrides
        defaultEnvironmentDatabase


fun getAppConfig(): AppConfig{
    val webConfig = createWebConfig(appEnv)
    return AppConfig(webConfig, from(appEnv), createJwtConfig(appEnv))
}
