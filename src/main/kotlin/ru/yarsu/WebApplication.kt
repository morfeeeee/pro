package ru.yarsu

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.filter.ServerFilters
import org.http4k.lens.*
import org.http4k.routing.ResourceLoader
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import ru.ac.uniyar.web.templates.ContextAwarePebbleTemplates
import ru.yarsu.config.getAppConfig
import ru.yarsu.domain.*
import ru.yarsu.web.filters.AutentificationFilter
import ru.yarsu.web.filters.NotFoundFilter
import ru.yarsu.web.router
import ru.yarsu.web.templates.ContextAwareViewRender
import java.io.File
import kotlin.concurrent.thread


fun main() {
    val objectMapper = jacksonObjectMapper()
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
    objectMapper.registerModule(JavaTimeModule())
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    val generation = Generation()
    val fileSpecialistsReade: File = if(!File(generation.fileName()).exists()){
        File(generation.createJsonFile())
    }
    else{
        File(generation.fileName())
    }
    val readerSpecialists = fileSpecialistsReade.readText()
    val listSpecialists: List<Person> = objectMapper.readValue(readerSpecialists)

    val listUsers: MutableList<User> = objectMapper.readValue(File("Users.json").readText())
    val userStorage = UserStorage(listUsers, listSpecialists.size)


    val listSalts: MutableList<Password> = objectMapper.readValue(File("Salts.json").readText())
    val saltStorage = SaltStorage(listSalts, listSpecialists.size)

    val unpackedPersonStorage = UnpackedPersonStorage(listSpecialists)
    val specialistsStorage = SpecialistsStorage(unpackedPersonStorage.getProfiles())
    val logger = LoggerFactory.getLogger("ru.yarsu.WebApplication")
    val loggingFilter = Filter { next: HttpHandler ->
        { request: Request ->
            val result = next(request)
            logger.atInfo().setMessage("Request").addKeyValue("URI", request.uri).log()
            result
        }
    }
    val appConfig = getAppConfig()


    val contexts = RequestContexts()


    val renderer = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRender.withContentType(renderer, ContentType.TEXT_HTML)
    val currentWorkerLens: RequestContextLens<User?> = RequestContextKey.optional(contexts, "user")
    val htmlViewWithContext = htmlView
        .associateContextLens("user", currentWorkerLens)


    val cookieLens: BiDiLens<Request, Cookie?> = Cookies.optional("auth")

    val userLens = RequestContextKey.optional<User>(contexts, "user")

    val jwtConfig = appConfig.jwtConfig

    val jwtTools = JwtTools(jwtConfig.secret, jwtConfig.organization, jwtConfig.tokenTimeLife)

    val autentificationFilter = AutentificationFilter(jwtTools, userLens, cookieLens, userStorage)

    val appWithStaticResources = routes(
        router(specialistsStorage, userStorage, saltStorage, userLens, htmlViewWithContext),
        static(ResourceLoader.Classpath("/ru/yarsu/public")),
    )
    val filters = ServerFilters.InitialiseRequestContext(contexts).then(autentificationFilter)
        .then(NotFoundFilter(htmlViewWithContext)).then(loggingFilter).then(appWithStaticResources)


    val server = filters.asServer(Netty(appConfig.webConfig.webPort)).start()


    println("Server started on http://localhost:" + server.port())
    println("Press enter to exit application.")
    val hook = thread(start = false) {
        val fileSpecialistsWrite = File("Specialists.json")
        val stringSpecialists = objectMapper.writeValueAsString(specialistsStorage.getInPersonFormat())
        fileSpecialistsWrite.writeText(stringSpecialists, Charsets.UTF_8)

        val fileUsersWrite = File("Users.json")
        val stringUsers = objectMapper.writeValueAsString(userStorage.getListUsers())
        fileUsersWrite.writeText(stringUsers, Charsets.UTF_8)

        val fileSaltsWrite = File("Salts.json")
        val stringSalts = objectMapper.writeValueAsString(saltStorage.getAllSalts())
        fileSaltsWrite.writeText(stringSalts, Charsets.UTF_8)

    }
    Runtime.getRuntime().addShutdownHook(hook)
    readln()
    server.stop()

}
