package com.player.dependency

import com.player.data.LoginStateRepository
import org.koin.dsl.module


val loginStateModule = module {
    single {
        LoginStateRepository(get())
    }
}