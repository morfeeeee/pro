package ru.yarsu.web.handlers


import org.http4k.core.*
import org.http4k.core.body.form
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.ErrorCategoryVM
import ru.yarsu.web.templates.ContextAwareViewRender

class PostNewCategoryHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val category = form.findSingle("category")
        if(category == null || category == "") return Response(Status.OK).with(htmlViewWithContext(request) of ErrorCategoryVM("", "Вы не ввели категорию"))
        if(category in storage.getListProfession()) return Response(Status.OK).with(htmlViewWithContext(request) of ErrorCategoryVM(category, "Такая услуга уже есть"))
        storage.addCategory(category)
        return Response(Status.FOUND).header("Location", "/")
    }
}