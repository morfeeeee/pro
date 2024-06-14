import org.http4k.core.*
import org.http4k.lens.BiDiLens
import ru.yarsu.domain.*
import ru.yarsu.web.models.MyAdvertisingVM
import ru.yarsu.web.templates.ContextAwareViewRender


class MyAdvertisingHandler(val specialistsStorage: SpecialistsStorage, val userLens: BiDiLens<Request, User?>, val htmlViewWithContext: ContextAwareViewRender
): HttpHandler {
    override fun invoke(request: Request): Response {
        val user = userLens(request)
        val listAdvertising = specialistsStorage.getListAdvertisingByUser(user!!.id)
        val translater = specialistsStorage.getTranslater()
        return Response(Status.OK).with(htmlViewWithContext(request) of MyAdvertisingVM(listAdvertising, translater))
    }
}