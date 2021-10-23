package com.player

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {

    private var userDao: UserDao? = null
    private var db: UserDatabase? = null

    var recyclerView:RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private val footerOnClick: (Int,String,String) -> Unit= { type, userLogin, userPassword ->
            if (type ==0)
            {
                lifecycleScope.launch(Dispatchers.IO) {
                    userDao?.insertUser(User(0, userLogin, userPassword))
                }
                itemOnCheckLog()
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (userDao?.getUserByLogin(userLogin)?.password == userPassword) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
}