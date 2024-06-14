package ru.yarsu.domain

class AllErrors(val formErrors: Map<String, List<String>>, val errorsSocialNetwork: List<String>) {


    fun getAllErrors(): List<String>{
        val listAllErrors = mutableListOf<String>()
        for(i in formErrors){
            listAllErrors += i.value
        }
        for(i in errorsSocialNetwork){
            listAllErrors += i
        }
        return listAllErrors.distinct()
    }

    fun formWithErrors(): Boolean{
        if(getAllErrors().isEmpty()) return false
        return true
    }


}