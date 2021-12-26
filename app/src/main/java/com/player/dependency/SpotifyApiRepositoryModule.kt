package com.player.dependency


import com.player.data.SpotifyApiRepository
import org.koin.dsl.module

val spotifyAPIRepositoryModule = module {
    single { SpotifyApiRepository() }
}