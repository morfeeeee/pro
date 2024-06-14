package ru.yarsu.domain

class PermissionsStorage() {
    val listRoles = mutableListOf<Role>()
    init{
        listRoles.add(Role("unauthorized", Permissions()))
        listRoles.add(Role("admin", Permissions(
            canAddAdvertising = true,
            canListUsers = true,
            canAddCategory = true,
            canDeleteCategory = true,
            canEditAdvertising = true,
            canDeleteAdvertising = true,
            canEditProfile = false
        )))
        listRoles.add(Role("moderator", Permissions(
            canAddAdvertising = false,
            canListUsers = false,
            canAddCategory = true,
            canDeleteCategory = true,
            canEditAdvertising = false,
            canDeleteAdvertising = false,
            canEditProfile = false
        )))
        listRoles.add(Role("specialist", Permissions(
            canAddAdvertising = true,
            canListUsers = false,
            canAddCategory = false,
            canDeleteCategory = false,
            canEditAdvertising = true,
            canDeleteAdvertising = true,
            canEditProfile = true
        )))
    }

    fun getPermisssions(role: String): Permissions{
        for(permission in listRoles){
            if(role == permission.role){
                return permission.permissions
            }
        }
        return Permissions()
    }
}