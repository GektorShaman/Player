package com.player.dependency


import com.player.data.UserRepository
import org.koin.dsl.module

val userRepositoryModule = module {
    single {
        UserRepository(get())
    }
}