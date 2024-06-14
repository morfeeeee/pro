package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.lens.BiDiLens
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.*
import ru.yarsu.web.models.ErrorsSpecialistBlankVM
import ru.yarsu.web.models.NewSpecialistVM
import ru.yarsu.web.templates.ContextAwareViewRender
import java.time.LocalDateTime

class PostNewSpecialistHandler(val addNewSpecialist: AddNewSpecialist,
                               val storage: SpecialistsStorage,
                               val userLens: BiDiLens<Request, User?>, val htmlViewWithContext: ContextAwareViewRender
): HttpHandler {
    override fun invoke(request: Request): Response {
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
        val errorsList = mutableListOf<String>()
        errorsList += errors.getAllErrors()
        val user = userLens(request)
        if(user == null) errorsList.add("Вы не можете добавить объявление, пока вы не вошли в систему")

        if(errorsList.isNotEmpty()){

            return Response(Status.OK).with(htmlViewWithContext(request) of ErrorsSpecialistBlankVM(name, surname,
                patronymic, phoneNumber, education1, education2, vkId, telegramId, facebookId, storage.getListProfession(), errorsList, category, licenseToBoolean))
        }
        val nameToString = errorsForm.convertNullToString(name)
        val surnameToString = errorsForm.convertNullToString(surname)
        val patronymicToString = errorsForm.convertNullToString(patronymic)
        val phoneNumberToString = errorsForm.convertNullToString(phoneNumber)
        val education1ToString = errorsForm.convertNullToString(education1)
        val education2ToString = errorsForm.convertNullToString(education2)
        val listSocialIdToString = errorsForm.delEmptyValue(listSocialId)
        val categoryToString = errorsForm.convertNullToString(category)

        val listEducation = mutableListOf<String>()
        if(education1ToString != "") listEducation.add(education1ToString)
        if(education2ToString != "") listEducation.add(education2ToString)


        val id = storage.getIdCounter()

        addNewSpecialist.append(Profile(nameToString, surnameToString,
            patronymicToString, phoneNumberToString,
            listEducation, listSocialIdToString, LocalDateTime.now(), licenseToBoolean, id, storage.getCategoryByName(categoryToString), user!!.id))

        val educationToEnglish = storage.getTranslateProfessionToEnglish(education1ToString)
        return Response(Status.FOUND).header("Location", "/profession$educationToEnglish/person$id")
    }
}