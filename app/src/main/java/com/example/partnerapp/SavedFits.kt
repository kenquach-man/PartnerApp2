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
        val homeButton : Button = findViewById(R.id.buttonHome)
        homeButton.setOnClickListener{
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }
        val clothingSet = intent?.getParcelableArrayListExtra<Clothing>("clothingSet") as List<Clothing>
        if (clothingSet == null) {
            Log.i("INFO_TAG", "Set is null")
        }
        runOnUiThread {
            // Setup RecyclerView after populating clothingList
            val recyclerView: RecyclerView = findViewById(R.id.recycler)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = ClothingAdapter(clothingSet, this)
            Log.i("INFO_TAG", "Set displayed")
        }
    }
}