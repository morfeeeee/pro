package ru.yarsu.domain

interface AddNewSpecialistOperation {
    fun append(person: Profile)
}
class AddNewSpecialist(
    private val storage: SpecialistsStorage
) : AddNewSpecialistOperation {
    override fun append(person: Profile) = storage.addSpecialists(person)
}