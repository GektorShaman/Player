package com.player.data

import com.player.data.database.User
import com.player.data.database.UserDatabase

class UserRepository(private val userDatabase: UserDatabase) {

    fun getUser(introducedLogin:String) = userDatabase.userDao().getUserByLogin(introducedLogin)

    fun addUser(user: User) = userDatabase.userDao().insertUser(user)
}