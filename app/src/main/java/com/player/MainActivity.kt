package com.player

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var settings: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_layout)

        val btn: Button = findViewById(R.id.logoutbutton)
        btn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        settings = getSharedPreferences("PreferencesName", MODE_PRIVATE )
        val editor: SharedPreferences.Editor = settings.edit()
        editor.putBoolean("session",false)
        editor.commit()
        val intent = Intent(applicationContext, RegistrationActivity::class.java)
        startActivity(intent)
    }
}
