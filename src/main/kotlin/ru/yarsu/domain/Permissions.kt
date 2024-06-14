package ru.yarsu.domain

data class Permissions (
    val canAddAdvertising: Boolean = false,
    val canListUsers: Boolean = false,
    val canAddCategory: Boolean = false,
    val canDeleteCategory: Boolean = false,
    val canEditAdvertising: Boolean = false,
    val canDeleteAdvertising: Boolean = false,
    val canEditProfile: Boolean = false,
)