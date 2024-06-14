package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.lens.BiDiLens
import ru.yarsu.domain.AddNewSpecialist
import ru.yarsu.domain.Profile
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.domain.User
import ru.yarsu.web.templates.ContextAwareViewRender
import java.time.LocalDateTime


class PostAddAdvertisingHandler(val addNewSpecialist: AddNewSpecialist,
                                val storage: SpecialistsStorage,
                                val userLens: BiDiLens<Request, User?>, val htmlViewWithContext: ContextAwareViewRender
): HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val license = form.findSingle("license")
        val category = form.findSingle("category")
        val licenseToBoolean: Boolean = license != null

        val user = userLens(request)
        val id = storage.getIdCounter()
        val profile = storage.getPersonByUserId(user!!.id)

        addNewSpecialist.append(
            Profile(profile!!.name, profile.surname,
            profile.patronymic, profile.phoneNumber,
            profile.education, profile.idSocialNetwork, LocalDateTime.now(), licenseToBoolean, id, storage.getCategoryByName(category!!), user.id)
        )
        return Response(Status.FOUND).header("Location", "/profession${storage.getTranslateProfessionToEnglish(category)}/person$id")
    }
}