package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.User

class UsersVM(val listUsers: List<User>): ViewModel
