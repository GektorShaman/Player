package com.player

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insertUser(user:User)

    @Query("SELECT * FROM User WHERE login = :introducedLogin")
    fun getUserByLogin(introducedLogin:String):User

    @Update
    fun updateUser(user:User)

    @Delete
    fun deleteUser(user:User)

}