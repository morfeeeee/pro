package ru.yarsu.web

import MyAdvertisingHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.lens.BiDiLens
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.yarsu.domain.*
import ru.yarsu.web.filters.PermissionFilter
import ru.yarsu.web.handlers.*
import ru.yarsu.web.templates.ContextAwareViewRender

fun router(specialistsStorage: SpecialistsStorage,
           userStorage: UserStorage, saltStorage: SaltStorage, userLens: BiDiLens<Request, User?>, htmlViewWithContext: ContextAwareViewRender
): RoutingHttpHandler{
    val addNewSpecialist = AddNewSpecialist(specialistsStorage)
    val getListSpecialistsByProfession = GetListSpecialistsByProfession(specialistsStorage)
    val specialistsHandler = SpecialistsHandler(specialistsStorage, getListSpecialistsByProfession, htmlViewWithContext)


    return routes(
        "/" bind Method.GET to HomeHandler(specialistsStorage, htmlViewWithContext),
        "/{profession}/page{id}" bind Method.GET to specialistsHandler,
        "/profession{profession}/person{id}" bind Method.GET to PersonHandler(specialistsStorage, htmlViewWithContext),
        "/addPerson" bind Method.GET to PermissionFilter(Permissions::canAddAdvertising, userLens, htmlViewWithContext).then(NewSpecialistHandler(specialistsStorage, userLens, htmlViewWithContext)),
        "/addPerson" bind Method.POST to PostNewSpecialistHandler(addNewSpecialist, specialistsStorage, userLens, htmlViewWithContext),
        "/editPerson{id}" bind Method.GET to PermissionFilter(Permissions::canEditAdvertising, userLens, htmlViewWithContext).then(PersonEditHandler(specialistsStorage, htmlViewWithContext)),
        "/editPerson{id}" bind Method.POST to PostPersonEditHandler(specialistsStorage, htmlViewWithContext),
        "/deletePerson{id}" bind Method.GET to PermissionFilter(Permissions::canDeleteAdvertising, userLens, htmlViewWithContext).then(GetDeletePersonHandler(specialistsStorage, htmlViewWithContext)),
        "/deletePerson{id}" bind Method.POST to DeletePersonHandler(specialistsStorage, htmlViewWithContext),
        "/addCategory" bind Method.GET to PermissionFilter(Permissions::canAddCategory, userLens, htmlViewWithContext).then(NewCategoryHandler(htmlViewWithContext)),
        "/addCategory" bind Method.POST to PostNewCategoryHandler(specialistsStorage, htmlViewWithContext),
        "/deleteCategory" bind Method.GET to PermissionFilter(Permissions::canDeleteCategory, userLens, htmlViewWithContext).then(DeleteCategoryHandler(specialistsStorage, htmlViewWithContext)),
        "/deleteCategory" bind Method.POST to PostDeleteCategoryHandler(specialistsStorage, htmlViewWithContext),
        "/addUser" bind Method.GET to AddUserHandler(htmlViewWithContext),
        "/addUser" bind Method.POST to PostAddUserHandler(userStorage, saltStorage, htmlViewWithContext),
        "/users" bind Method.GET to PermissionFilter(Permissions::canListUsers, userLens, htmlViewWithContext).then(UsersHandler(userStorage, htmlViewWithContext)),
        "/login" bind Method.GET to LoginHandler(htmlViewWithContext),
        "/login" bind Method.POST to PostLoginHandler(userStorage, saltStorage, specialistsStorage, htmlViewWithContext),
        "/logout" bind Method.GET to OutHandler(),
        "/addAdvertising" bind Method.GET to PermissionFilter(Permissions::canAddAdvertising, userLens, htmlViewWithContext).then(AddAdvertisingHandler(userLens, specialistsStorage, htmlViewWithContext)),
        "/addAdvertising" bind Method.POST to PostAddAdvertisingHandler(addNewSpecialist, specialistsStorage, userLens, htmlViewWithContext),
        "/editAdvertising{id}" bind Method.GET to PermissionFilter(Permissions::canEditAdvertising, userLens, htmlViewWithContext).then(EditAdvertisingHandler(specialistsStorage, htmlViewWithContext)),
        "/editAdvertising{id}" bind Method.POST to PostEditAdvertisingHandler(specialistsStorage, userLens, htmlViewWithContext),
        "/profile" bind Method.GET to PermissionFilter(Permissions::canEditProfile, userLens, htmlViewWithContext).then(ProfileHandler(userLens, specialistsStorage, htmlViewWithContext)),
        "/editUser{id}" bind Method.GET to PermissionFilter(Permissions::canListUsers, userLens, htmlViewWithContext).then(EditUserHandler(userStorage, htmlViewWithContext)),
        "/editUser{id}" bind Method.POST to PostEditUserHandler(userStorage, htmlViewWithContext),
        "/myAdvertising" bind Method.GET to PermissionFilter(Permissions::canEditProfile, userLens, htmlViewWithContext).then(MyAdvertisingHandler(specialistsStorage, userLens, htmlViewWithContext)),

    )


}


