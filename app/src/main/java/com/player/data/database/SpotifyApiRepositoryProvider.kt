package com.player.data.database

import com.player.data.SpotifyApiRepository

object SpotifyApiRepositoryProvider {
    fun provideSpotifyApiRepository() : SpotifyApiRepository {
        return SpotifyApiRepository()
    }
}