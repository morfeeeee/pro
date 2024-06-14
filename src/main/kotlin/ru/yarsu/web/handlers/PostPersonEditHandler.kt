package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.routing.path
import ru.yarsu.domain.ErrorsFormHandler
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.ErrorsSpecialistBlankVM
import ru.yarsu.web.templates.ContextAwareViewRender

class PostPersonEditHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        val person = storage.getPersonById(id) ?: return Response(Status.NOT_FOUND)
        val form = request.form()
        val name = form.findSingle("name")
        val surname = form.findSingle("surname")
        val patronymic = form.findSingle("patronymic")
        val phoneNumber = form.findSingle("phoneNumber")
        val education1 = form.findSingle("education1")
        val education2 = form.findSingle("education2")
        val vk = form.findSingle("vk")
        val telegram = form.findSingle("telegram")
        val facebook = form.findSingle("facebook")
        val license = form.findSingle("license")
        val category = form.findSingle("category")
        val listSocialId = listOf<String>("Vk:$vk", "Telegram:$telegram", "Facebook:$facebook")
        val licenseToBoolean: Boolean = license != null

        val errorsForm = ErrorsFormHandler(name, surname, patronymic, phoneNumber, education1, education2, listSocialId, category)
        val errors = errorsForm.getAllErrorsInForm()

        val vkId = listSocialId[0].removePrefix("Vk:")
        val telegramId = listSocialId[1].removePrefix("Telegram:")
        val facebookId = listSocialId[2].removePrefix("Facebook:")

        if(errors.formWithErrors()){
            println(category)
            return Response(Status.OK).with(htmlViewWithContext(request) of ErrorsSpecialistBlankVM(name, surname,
                patronymic, phoneNumber, education1, education2, vkId, telegramId, facebookId, storage.getListProfession(), errors.getAllErrors(), category, licenseToBoolean))
        }
        val nameToString = errorsForm.convertNullToString(name)
        val surnameToString = errorsForm.convertNullToString(surname)
        val patronymicToString = errorsForm.convertNullToString(patronymic)
        val phoneNumberToString = errorsForm.convertNullToString(phoneNumber)
        val education1ToString = errorsForm.convertNullToString(education1)
        val education2ToString = errorsForm.convertNullToString(education2)
        val listSocialIdToString = errorsForm.delEmptyValue(listSocialId)
        val categoryToString = errorsForm.convertNullToString(category)
        storage.editPerson(person, nameToString, surnameToString, patronymicToString, phoneNumberToString, education1ToString, education2ToString, listSocialIdToString, licenseToBoolean, categoryToString)

        val educationToEnglish = storage.getTranslateProfessionToEnglish(education1ToString)
        return Response(Status.FOUND).header("Location", "/profession$educationToEnglish/person$id")
    }
}