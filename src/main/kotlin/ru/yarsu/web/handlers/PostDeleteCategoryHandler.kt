package ru.yarsu.web.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import ru.yarsu.domain.SpecialistsStorage
import ru.yarsu.web.models.ErrorDeleteCategoryVM
import ru.yarsu.web.templates.ContextAwareViewRender

class  PostDeleteCategoryHandler(val storage: SpecialistsStorage, val htmlViewWithContext: ContextAwareViewRender): HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val category = form.findSingle("category")
        if(category == null || category == "") return Response(Status.OK).with(htmlViewWithContext(request) of ErrorDeleteCategoryVM(storage.getListProfession(), "Вы не ввели категорию", ""))
        if(!storage.deleteCategory(category)){
            return Response(Status.OK).with(htmlViewWithContext(request) of ErrorDeleteCategoryVM(storage.getListProfession(), "По данной категории услуг еще есть объявления", category))
        }
        return Response(Status.FOUND).header("Location", "/")
    }
}