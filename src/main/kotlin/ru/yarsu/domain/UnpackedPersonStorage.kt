package ru.yarsu.domain

class UnpackedPersonStorage (val personList: List<Person>){
    fun getProfiles(): List<Profile>{
        val listProfiles = mutableListOf<Profile>()
        for(person in personList){
            for(advertising in person.listAdvertising){
                listProfiles.add(Profile(person.name, person.surname, person.patronymic, person.phoneNumber,
                    person.education, person.idSocialNetwork, advertising.dateAdd,
                    advertising.license, advertising.id, advertising.category, person.id))
            }
        }
        return listProfiles
    }
}