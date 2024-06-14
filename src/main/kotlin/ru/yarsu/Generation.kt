package ru.yarsu

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.yarsu.domain.*
import java.io.File
import java.security.MessageDigest
import java.time.LocalDateTime



fun rand(start: Int, end: Int): Int {
    require(start <= end) { "Illegal Argument" }
    return (start..end).random()
}

fun createNumberPhone(): String{
    var number = "89"
    for(i in 1..9){
        number += rand(0, 9).toString()
    }
    return number
}

fun createID(social_network: Int): String{
    val listID = listOf<String>("Vk", "Telegram", "Facebook")
    var numberID = ""
    for(i in 1..rand(4,7)){
        numberID += rand(0, 9).toString()
    }
    return listID[social_network] + ": @" + numberID
}

fun createDate(): LocalDateTime{
    var dateNow: LocalDateTime = LocalDateTime.now()
    dateNow = dateNow.minusDays(rand(0,365).toLong())
    dateNow = dateNow.minusHours(rand(0,24).toLong())
    dateNow = dateNow.minusMinutes(rand(0,60).toLong())
    return dateNow
}

fun createlicense(educationList: List<String>, category: String): Boolean{
    if(category in educationList) return true
    return false
}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun createSalt(id: Int): Password{
    return Password(id, getRandomString(101))
}

fun createPassword(password: Password, value: String): Password{
    val hexPassword = value + password.password
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(hexPassword.toByteArray())
    return Password(password.id, digest.joinToString("") { "%02x".format(it) })
}




fun createListUsers(listPersons: List<Person>, salts: List<Password>): List<User>{
    val listUsers = mutableListOf<User>()
    for(person in listPersons){
        for(salt in salts){
            if(salt.id == person.id) {
                listUsers.add(
                    User(
                        person.phoneNumber, createPassword(salt, "12345"), createDate(),
                        person.id, "specialist"
                    )
                )
            }
        }
    }
    for(salt in salts){
        if(salt.id == 1001){
            listUsers.add(User("admin", createPassword(salt, "admin"), createDate(),
                salt.id,"admin")
            )
        }
        if(salt.id == 1002){
            listUsers.add(User("moderator", createPassword(salt, "moderator"), createDate(),
                salt.id,"moderator")
            )
        }
    }
    return listUsers
}



class Generation {

    val names = listOf<String>("Петр", "Сергей", "Виктор", "Александр", "Даниил", "Максим", "Алексей", "Владимир", "Роман", "Никита")
    val surnames = listOf<String>("Крылов", "Пушкин", "Лермонтов", "Грибоедов", "Есенин", "Маяковский", "Толстой", "Гоголь", "Некрасов", "Пришвин")
    val patronymics = listOf<String>("Викторович", "Михаилович", "Сергеевич", "Петрович", "Иванович", "Александрович", "Алексеевич", "Валерьевич", "Максимович", "Романович")
    val education = listOf<String>("Программист", "Математик", "Плотник", "Электрик", "Хирург", "Сантехник", "Акушер", "Монтажник", "Инженер", "Тестировщик", "Юрист")
    val personList = mutableListOf<Person>()

    fun fileName(): String{
        return "Specialists.json"
    }

    fun createJsonFile(): String{
        val listCategory = mutableListOf<Category>()
        var count = 1
        for(i in education){
            listCategory.add(Category(count++, i))
        }
        count = 1
        for(name in names){
            for(surname in surnames){
                for(patronymic in patronymics){
                    val educationList = mutableListOf<String>()
                    val index = rand(0, education.size - 1)
                    educationList.add(education[index])
                    val index2 = rand(0, education.size - 1)
                    if(index != index2){
                        educationList.add(education[index2])
                    }
                    val listID = mutableListOf<String>()
                    for(i in 0..rand(1, 2)){
                        listID.add(createID(i))
                    }
                    val category = listCategory[rand(0, listCategory.size - 1)]
                    val listAdvertising = listOf<Advertising>(Advertising(category, createlicense(educationList, category.name), createDate(), count++))
                    personList.add(Person(listAdvertising[0].id, name, surname, patronymic, createNumberPhone(), educationList, listID, listAdvertising))
                }
            }
        }
        val salts = mutableListOf<Password>()
        for(person in personList){
            salts.add(createSalt(person.id))
        }
        salts.add(Password(salts.size + 1, getRandomString(101)))
        salts.add(Password(salts.size + 1, getRandomString(101)))
        val users = createListUsers(personList, salts)
        File("Users.json").createNewFile()
        val objectMapper = jacksonObjectMapper()
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val userString = objectMapper.writeValueAsString(users)
        val saltsString = objectMapper.writeValueAsString(salts)
        File("Users.json").writeText(userString, Charsets.UTF_8)
        File("Salts.json").writeText(saltsString, Charsets.UTF_8)


        val fileSpecialistsWrite = File("Specialists.json")
        val stringSpecialists = objectMapper.writeValueAsString(personList)
        fileSpecialistsWrite.writeText(stringSpecialists, Charsets.UTF_8)
        return fileName()
    }
}