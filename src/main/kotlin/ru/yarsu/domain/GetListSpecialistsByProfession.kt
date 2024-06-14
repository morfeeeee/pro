package ru.yarsu.domain


interface GetListSpecialistsOperation {
    fun get(profession: String): List<Profile>
}
class GetListSpecialistsByProfession(
    private val storage: SpecialistsStorage
) : GetListSpecialistsOperation {
    override fun get(profession: String) = storage.specialistsByProfession(profession)
}
