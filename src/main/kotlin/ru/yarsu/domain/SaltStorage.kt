package ru.yarsu.domain

class SaltStorage(private val passwords: List<Password>, private val startId: Int) {
    val passwordsMap = mutableMapOf<Int, Password>()
    var idCounter: Int = startId + 1

    init{
        for(password in passwords){
            passwordsMap[password.id] = password
            idCounter++
        }
    }

    fun getAllSalts(): List<Password>{
        return passwordsMap.values.toMutableList()
    }


    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun addPassword(password: Password){
        passwordsMap[password.id] = Password(password.id, getRandomString(101))
        idCounter++
    }

    fun getSalt(id: Int): Password? {
        return passwordsMap[id]
    }
}