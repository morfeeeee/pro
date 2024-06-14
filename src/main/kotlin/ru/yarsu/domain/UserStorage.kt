package ru.yarsu.domain

import java.security.MessageDigest

class UserStorage(private val listUsers: List<User>, private val startId: Int) {
    private val usersMap = mutableMapOf<Int, User>()
    private var idCounter: Int = startId + 1

    init {
        for(user in listUsers){
            addUser(user)
        }
    }

    fun addUser(user: User){
        usersMap[user.id] = user
        idCounter++
    }


    fun getListUsers(): List<User>{
        return usersMap.values.toMutableList()
    }

    fun identificationLogin(login: String): Map<Boolean, User>{
        val mapIdentification = mutableMapOf<Boolean, User>()
        for(user in getListUsers()){
            if(user.login == login) {
                mapIdentification[true] = user
                return mapIdentification
            }
        }
        mapIdentification[false] = getListUsers()[0]
        return mapIdentification
    }

    fun getUserById(id: Int): User?{
        for(user in getListUsers()){
            if(user.id == id) return user
        }
        return null
    }

    fun identificationUser(password: String, salt: String): String{
        val hexPassword = password + salt
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(hexPassword.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun editUser(user: User, role: String){
        usersMap.remove(user.id, user)
        usersMap[user.id] = User(user.login, user.password, user.dateAdd, user.id, role)
    }




}