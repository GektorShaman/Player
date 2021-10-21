package com.player

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegistrationActivity : AppCompatActivity() {

    var recyclerView:RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = RegistrationAdapter(getRegDataset(),footerOnClick,::itemOnCheckReg)
    }

    private fun getRegDataset(): List<String> {
        return this.resources.getStringArray(R.array.registration_dataset).toList()
    }

    private fun getLogDataset(): List<String> {
        return this.resources.getStringArray(R.array.logging_dataset).toList()
    }

    private fun itemOnCheckReg(){
        recyclerView?.adapter = RegistrationAdapter(getRegDataset(),footerOnClick,::itemOnCheckLog)
    }

    private fun itemOnCheckLog() {
        recyclerView?.adapter = RegistrationAdapter(getLogDataset(),footerOnClick,::itemOnCheckReg)
    }

    private val footerOnClick: (View, Int, Int) -> Unit={ view, position, type ->
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}