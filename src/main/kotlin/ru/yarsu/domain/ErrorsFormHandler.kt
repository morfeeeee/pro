package ru.yarsu.domain

class ErrorsFormHandler(val name: String?, val surname: String?, val patronymic: String?,
                        val phoneNumber: String?, val education1: String?, val education2: String?,
                        val idSocialNetwork: List<String>, val category: String?) {

    fun isPhoneNumber(value: String): Boolean{
        if(value[0] != '8'){
            return false
        }
        for(i in value){
            if(!i.isDigit()){
                return false
            }
        }
        return true
    }

    fun messagesForName(value: String?): List<String>{
        val errorsList = mutableListOf<String>()
        val valueWithoutSpaces = value?.replace(" ", "")
        if(valueWithoutSpaces == "" || value == null){
            errorsList.add("Поля с инициалами не должны быть пустыми")
            return errorsList
        }
        if(value[0].isDigit()){
            errorsList.add("Инициалы не могут начинаться с цифры")
        }
        return errorsList
    }

    fun messagesForPhoneNumber(value: String?): List<String>{
        val errorsList = mutableListOf<String>()
        if(value == "" || value == null){
            errorsList.add("Поле с телефоном не должно быть пустым")
            return errorsList
        }
        if(!isPhoneNumber(value) ){
            errorsList.add("Не коректный номер телефона")
        }
        return errorsList
    }

    fun messagesForEducation(value1: String?, value2: String?): List<String>{
        val errorsList = mutableListOf<String>()
        if((value1 == "" || value1 == null) && (value2 == "" || value2 == null) ){
            errorsList.add("Поле с образованием не должно быть пустым")
        }
        return errorsList
    }

    fun emptyListId(listId: List<String>): Boolean{
        if(listId[0] == "Vk:" && listId[1] == "Telegram:" && listId[2] == "Facebook:") return true
        return false
    }

    fun correctSocialId(listId: List<String>): Boolean{
        var count = 0
        for(i in listId){
            if(i.contains('@')) count++
        }
        if(count == 0) return false
        return true
    }

    fun messagesForSocialId(listOfValue: List<String>): List<String>{
        val errorsList = mutableListOf<String>()
        if(emptyListId(listOfValue)){
            errorsList.add("Не указаны ссылки на социальные сети")
            return errorsList
        }
        if(!correctSocialId(listOfValue)){
            errorsList.add("Ссылки на социальные сети должны содержать @")
        }
        return errorsList
    }

    fun convertNullToString(value: String?): String{
        if(value == null) return ""
        return value
    }

    fun delEmptyValue(listId: List<String>): List<String>{
        val newListId = mutableListOf<String>()
        for(id in listId){
            if(id == "Vk:" || id == "Telegram:" || id == "Facebook:"){
                newListId.add("")
            }
            else{
                newListId.add(id)
            }
        }
        return newListId
    }

    fun messagesForCategory(value: String?): List<String>{
        val errorsList = mutableListOf<String>()
        if(value == "" || value == null){
            errorsList.add("Вы не ввели категорию услуги")
        }
        return errorsList
    }

    fun getAllErrorsInForm(): AllErrors {
        val formErrors = mutableMapOf<String, List<String>>()
        formErrors[convertNullToString(name)] = messagesForName(name)
        formErrors[convertNullToString(surname)] = messagesForName(surname)
        formErrors[convertNullToString(patronymic)] = messagesForName(patronymic)
        formErrors[convertNullToString(phoneNumber)] = messagesForPhoneNumber(phoneNumber)
        formErrors[convertNullToString(education1)] = messagesForEducation(education1, education2)
        formErrors[convertNullToString(category)] = messagesForCategory(category)
        return AllErrors(formErrors, messagesForSocialId(idSocialNetwork))
    }

}