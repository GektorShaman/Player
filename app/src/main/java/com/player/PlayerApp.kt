package com.player

import android.app.Application
import com.player.dependency.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlayerApp)
            modules(
                listOf(
                    loginStateModule,
                    userRepositoryModule,
                    spotifyAPIRepositoryModule,
                    loginViewModelModule,
                    dataBaseModule
                )
            )
        }
    }
}