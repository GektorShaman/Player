package com.player.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.player.R
import com.player.data.database.User
import com.player.helpers.RegistrationAdapter
import com.player.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {

    private val userViewModel by viewModel<UserViewModel>()
    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (userViewModel.loginStateRepository.isLogged())
        {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = RegistrationAdapter(
            applicationContext,
            getRegDataset(),
            footerOnClick,
            ::itemOnCheckReg
        )
    }

    private fun getRegDataset(): List<String> {
        return this.resources.getStringArray(R.array.registration_dataset).toList()
    }

    private fun getLogDataset(): List<String> {
        return this.resources.getStringArray(R.array.logging_dataset).toList()
    }
    private fun itemOnCheckReg() {
        recyclerView?.adapter = RegistrationAdapter(
            applicationContext,
            getRegDataset(),
            footerOnClick,
            ::itemOnCheckLog
        )
    }

    private fun itemOnCheckLog() {
        recyclerView?.adapter = RegistrationAdapter(
            applicationContext,
            getLogDataset(),
            footerOnClick,
            ::itemOnCheckReg
        )
    }

    private val footerOnClick: (Int, String, String) -> Unit =
        work@{ type, userLogin, userPassword ->
            if (type == 0) {
                val user = User(
                    0,
                    userLogin,
                    userPassword,
                )
                userViewModel.registerUser(user)
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

            } else {
                userViewModel.tryLoginUser(userLogin, userPassword)

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

            }
        }
}