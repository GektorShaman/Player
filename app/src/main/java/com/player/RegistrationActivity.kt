package com.player

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {

    private var userDao: UserDao? = null
    private var db: UserDatabase? = null
    private lateinit var settings: SharedPreferences

    var recyclerView:RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = getSharedPreferences("PreferencesName", MODE_PRIVATE )
        if (settings.getBoolean("session",false)) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = RegistrationAdapter(applicationContext,getRegDataset(),footerOnClick,::itemOnCheckReg)

        db = UserDatabase.getUserDataBase(this)
        userDao = db?.userDao()
    }

    private fun getRegDataset(): List<String> {
        return this.resources.getStringArray(R.array.registration_dataset).toList()
    }

    private fun getLogDataset(): List<String> {
        return this.resources.getStringArray(R.array.logging_dataset).toList()
    }

    private fun itemOnCheckReg(){
        recyclerView?.adapter = RegistrationAdapter(applicationContext,getRegDataset(),footerOnClick,::itemOnCheckLog)
    }

    private fun itemOnCheckLog() {
        recyclerView?.adapter = RegistrationAdapter(applicationContext,getLogDataset(),footerOnClick,::itemOnCheckReg)
    }

    private val footerOnClick: (Int,String,String) -> Unit= work@{ type, userLogin, userPassword ->
            if (type ==0)
            {
                lifecycleScope.launch(Dispatchers.IO) {
                    userDao?.insertUser(User(0, userLogin, userPassword))
                }
            } else {
                var isCorrectPassword = false
                lifecycleScope.launch(Dispatchers.IO) {
                    isCorrectPassword= userDao?.getUserByLogin(userLogin)?.password == userPassword
                }
                if (!isCorrectPassword) {
                        val toast = Toast.makeText(applicationContext, "Неоотсветсвие пароля и логина", Toast.LENGTH_SHORT)
                        toast.show()
                        return@work
                    }
            }
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putBoolean("session", true)
            editor.commit()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)

        }
}