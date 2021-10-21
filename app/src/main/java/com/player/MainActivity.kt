package com.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = RegistrationAdapter(getRegDataset())
    }

    private fun getRegDataset(): List<String> {
        return this.resources.getStringArray(R.array.registration_dataset).toList()
    }

    private fun getLogDataset(): List<String> {
        return this.resources.getStringArray(R.array.logging_dataset).toList()
    }
}