package com.player.dependency


import com.player.data.database.UserDatabase
import org.koin.dsl.module

val dataBaseModule = module {
    single {
        UserDatabase.getUserDataBase(get())
    }
}