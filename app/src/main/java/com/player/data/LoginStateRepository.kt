package com.player.data

import android.content.Context

class LoginStateRepository(context: Context) {
    private val settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE)

    fun saveLoginState(newState: Boolean) {
        val editor = settings.edit()

        editor.putBoolean("session", newState)
        editor.apply()
    }

    fun isLogged() = settings.getBoolean("session", false)
}