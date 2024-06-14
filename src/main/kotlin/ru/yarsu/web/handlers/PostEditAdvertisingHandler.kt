package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.lens.BiDiLens
import org.http4k.routing.path
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.domain.User
import ru.yarsu.web.templates.ContextAwareViewRender


class PostEditAdvertisingHandler(
                                 val storage: SpecialistsStorage,
                                 val userLens: BiDiLens<Request, User?>, val htmlViewWithContext: ContextAwareViewRender
): HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val license = form.findSingle("license")
        val category = form.findSingle("category")
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        val licenseToBoolean: Boolean = license != null

        val profile = storage.getPersonById(id)
        val education2: String = if(profile!!.education.size == 2){
            profile.education[2]
        } else{
            ""
        }

        storage.editPerson(profile, profile.name, profile.surname,
            profile.patronymic, profile.phoneNumber, profile.education[0],
            education2, profile.idSocialNetwork, licenseToBoolean, category!!)
        return Response(Status.FOUND).header("Location", "/profession${storage.getTranslateProfessionToEnglish(category)}/person$id")
    }
}