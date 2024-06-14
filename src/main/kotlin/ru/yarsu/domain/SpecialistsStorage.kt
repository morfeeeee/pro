package ru.yarsu.domain

class SpecialistsStorage(private val listSpecialists: List<Profile>) {
    private val specialistsMap = mutableMapOf<Int, Profile>()
    private var idCounter: Int = 1
    private var counterCategory = 1
    private val translater = mutableMapOf<String, String>()
    private var idCategory = 1
    private val listCategorys = mutableListOf<Category>()

    init{
        for(specialist in listSpecialists){
            addSpecialists(specialist)
        }
        editTranslater()
    }

    fun getCategoryByName(categoryName: String): Category{
        for(category in listCategorys){
            if(category.name == categoryName){
                return category
            }
        }
        return listCategorys[0]
    }

    fun editTranslater(){
        for(person in listSpecialists){
            if(person.category.name !in translater){
                translater[person.category.name] = person.category.id.toString()
                listCategorys.add(person.category)
                idCategory++
            }
        }
    }

    fun getTranslater(): Map<String, String>{
        return translater
    }



    fun editPerson(person: Profile, name: String, surname: String,
                   patronumic: String, phoneNumber: String, education1: String, education2: String,
                   idSocialNetwork: List<String>, license: Boolean, category: String){
        val listEducation = mutableListOf<String>()
        if(education1 != "") listEducation.add(education1)
        if(education2 != "") listEducation.add(education2)
        val newPerson = Profile(name, surname, patronumic, phoneNumber,
            listEducation, idSocialNetwork, person.dateAdd, license, person.id, getCategoryByName(category), person.personId)
        specialistsMap.remove(person.id, person)
        specialistsMap[newPerson.id] = newPerson
    }

    fun getAllPersons(): List<Profile>{
        return specialistsMap.values.toMutableList()
    }

    fun getProfessionListToEnglish(): List<String>{
        val list = mutableListOf<String>()
        for(i in translater){
            list.add(i.value)
        }
        return list
    }

    fun getTranslateProfessionToEnglish(profession: String): String{
        return translater[profession].toString()
    }

    fun getPersonById(id: Int): Profile?{
        return specialistsMap[id]

    }

    fun getIdCounter(): Int{
        return idCounter
    }

    fun getIdCategory(): Int{
        return idCategory
    }

    fun addSpecialists(person: Profile){
        val id = person.id
        specialistsMap[id] = person
        idCounter++
        if(person.category.name !in translater.keys){
            translater[person.category.name] = person.category.id.toString()
            listCategorys.add(person.category)
            idCategory++
        }
    }

    fun getPersonByUserId(id: Int): Profile?{
        for(person in getAllPersons()){
            if(id == person.personId) return person
        }
        return null
    }

    fun filterLicense(license: Boolean, listPersons: List<Profile>):List<Profile>{
        val filterList = mutableListOf<Profile>()
        if(license){
            for(person in listPersons){
                if(person.license){
                    filterList.add(person)
                }
            }
            return filterList
        }
        return listPersons
    }

    fun filterCountEducation(countEducation: Int?, listPersons: List<Profile>): List<Profile>{
        val filterList = mutableListOf<Profile>()
        if(countEducation != null){
            for(person in listPersons){
                if(person.education.size == countEducation){
                    filterList.add(person)
                }
            }
            return filterList
        }
        return listPersons
    }

    fun filterLicenseAndCountEducation(countEducation: Int?, license: Boolean): List<Profile>{
        val filterByLicense = filterLicense(license, getAllPersons())
        return filterCountEducation(countEducation, filterByLicense)
    }

    fun specialistsByProfession(profession: String): List<Profile>{
        val listOfThisSpecialty = mutableListOf<Profile>()
        for(person in getAllPersons()){
            if(profession == person.category.name) listOfThisSpecialty.add(person)
        }
        return listOfThisSpecialty
    }
    fun getListProfession(): List<String>{
        return translater.keys.toMutableList()
    }
    fun specialistsByPage(pageNumber: Int): List<Profile>{
        val filterListSpecialists = mutableListOf<Profile>()
        val rangeBottom = (pageNumber - 1) * 6
        val rangeTop: Int
        rangeTop = if((rangeBottom + 5) >= getAllPersons().size - 1){
            getAllPersons().size - 1
        } else{
            rangeBottom + 5
        }
        for(i in rangeBottom ..rangeTop){
            filterListSpecialists.add(getAllPersons()[i])
        }
        return filterListSpecialists
    }


    fun pageAmount(): Int{
        if(getAllPersons().size % 6 == 0){
            return getAllPersons().size / 6
        }
        return (getAllPersons().size / 6) + 1
    }

    fun deletePerson(person: Profile){
         specialistsMap.remove(person.id, person)
        if(specialistsByProfession(person.category.name).isEmpty()){
            translater.remove(person.category.id.toString())
        }
    }

    fun deleteCategory(category: String): Boolean{
        if(specialistsByProfession(category).isEmpty()){
            translater.remove(category)
            return true
        }
        return false
    }

    fun addCategory(category: String){
        translater[category] = (idCategory).toString()
        listCategorys.add(Category(idCategory, category))
        idCategory++

    }


    fun getTranslateProfessionToRussia(profession: String): String{
        var key = ""
        for(prof in translater){
            if(prof.value == profession){
                key = prof.key
                break
            }
        }
        return key
    }

    fun personInList(id: Int, listPersons: List<Person>): Boolean{
        for(person in listPersons){
            if(id == person.id) return true
        }
        return false
    }

    fun getInPersonFormat(): List<Person>{
        val listProfiles = getAllPersons()
        val listPersons = mutableListOf<Person>()
        for(profile in listProfiles){
            val listAdvertising = mutableListOf<Advertising>(Advertising(profile.category, profile.license, profile.dateAdd, profile.id))
            val person = Person(profile.personId, profile.name, profile.surname, profile.patronymic,
                profile.phoneNumber, profile.education, profile.idSocialNetwork, listAdvertising)
            listPersons.add(person)
        }
        return listPersons
    }

    fun getListAdvertisingByUser(id: Int): List<Profile>{
        val listAdvertising = mutableListOf<Profile>()
        for(profile in getAllPersons()){
            if(profile.personId == id) listAdvertising.add(profile)
        }
        return listAdvertising
    }

}