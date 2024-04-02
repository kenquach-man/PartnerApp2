package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedFits : AppCompatActivity() {
    var savedFits = mutableListOf<Fit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_fits)

        val homeButton : Button = findViewById(R.id.buttonHome)

        homeButton.setOnClickListener{
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }

        val clothingSet = intent?.getParcelableArrayListExtra<Clothing>("clothingSet") as MutableList<Clothing>
        if (clothingSet == null) {
            Log.i("INFO_TAG", "Set is null")
        } else { // Else if there was a clothing set saved
            val fit = Fit(savedFits.size, clothingSet)
            savedFits.add(fit)
            Log.i("INFO_TAG", "Fit has been added to savedFits")
            Log.i("INFO_TAG", "Size of savedFits is ${savedFits.size}")

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