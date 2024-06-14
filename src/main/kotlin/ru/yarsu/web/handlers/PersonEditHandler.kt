package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.PersonEditVM
import ru.yarsu.web.templates.ContextAwareViewRender

class PersonEditHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        if(storage.getPersonById(id) == null) return Response(Status.NOT_FOUND)
        val person = storage.getPersonById(id)
        val listSocialId = person?.idSocialNetwork ?: return Response(Status.NOT_FOUND)
        var vkId = ""
        var telegramId = ""
        var facebookId = ""
        for(idSocial in listSocialId){
            if(idSocial.contains("Vk:")) vkId = idSocial.removePrefix("Vk:")
            if(idSocial.contains("Telegram:")) telegramId = idSocial.removePrefix("Telegram:")
            if(idSocial.contains("Facebook:")) facebookId = idSocial.removePrefix("Facebook:")
        }
        return Response(Status.OK).with(htmlViewWithContext(request) of PersonEditVM(person, vkId, telegramId, facebookId, storage.getListProfession()))
    }
}