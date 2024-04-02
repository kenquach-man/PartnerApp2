package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedFits : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_fits)
        val savedFits =
            intent?.getParcelableArrayListExtra<Fit>("savedFits") as MutableList<Fit>
        val homeButton : Button = findViewById(R.id.buttonHome)

        homeButton.setOnClickListener{
            val homeIntent = Intent(this, MainActivity::class.java)
            homeIntent.putParcelableArrayListExtra("savedFits", ArrayList(savedFits))
            startActivity(homeIntent)
        }

        updateUIWithSavedFits(savedFits)
    }

    fun updateUIWithSavedFits(savedFits: MutableList<Fit>) {
        // Update the UI with the clothing set
        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FitsAdapter(savedFits, this)
    }
}