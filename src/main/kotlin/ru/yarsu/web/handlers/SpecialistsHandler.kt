package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.yarsu.domain.GetListSpecialistsByProfession
import ru.yarsu.domain.Paginator
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.SpecialistsVM
import ru.yarsu.web.templates.ContextAwareViewRender


class SpecialistsHandler(val firstStorage: SpecialistsStorage,
                         val getListSpecialistsByProfession: GetListSpecialistsByProfession, val htmlViewWithContext: ContextAwareViewRender
): HttpHandler {
    override fun invoke(request: Request): Response {
        val profession: String = request.path("profession") ?: return Response(Status.NOT_FOUND)
        if(profession !in firstStorage.getProfessionListToEnglish()){
            return Response(Status.NOT_FOUND)
        }
        val numberPage: Int = request.path("id")?.toIntOrNull() ?: return Response(Status.NOT_FOUND)
        if(numberPage <= 0) return Response(Status.NOT_FOUND)

        val parameters = request.uri.queries()
        var countEducation = parameters.findSingle("countEducation")
        val license = parameters.findSingle("license")
        var countEducationToInt: Int? = null
        val licenseToBoolean: Boolean = license != null

        if(countEducation != null){
            if(!countEducation.isEmpty()){
                for(i in countEducation){
                    if(!i.isDigit()) return Response(Status.NOT_FOUND)
                }
                countEducationToInt = countEducation.toInt()
            }
        }
        else countEducation = ""

        val professionToRussia = firstStorage.getTranslateProfessionToRussia(profession)
        val listSpecialistsByProfession = getListSpecialistsByProfession.get(professionToRussia)
        val specialistsStorage = SpecialistsStorage(SpecialistsStorage(listSpecialistsByProfession.sortedBy { it.dateAdd }).
        filterLicenseAndCountEducation(countEducationToInt, licenseToBoolean))
        if(numberPage > specialistsStorage.pageAmount() && specialistsStorage.pageAmount()!= 0) return Response(Status.NOT_FOUND)


        val paginator = Paginator(request.uri, numberPage, specialistsStorage.pageAmount(), profession)
        val listPages = paginator.getTwoPagesAroundCurrent()
        val listSpecialistsByPage = specialistsStorage.specialistsByPage(numberPage)
        val emptyOrNot: Int = if(listSpecialistsByPage.isEmpty()){
            0
        } else{
            1
        }
        return Response(Status.OK).with(htmlViewWithContext(request) of SpecialistsVM(listSpecialistsByPage, firstStorage.getTranslater(), listPages, professionToRussia,
            "?"+ request.uri.query, emptyOrNot, countEducation, licenseToBoolean))
    }
}