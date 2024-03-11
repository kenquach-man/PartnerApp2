package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SavedFits : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_fits)
        
        val homeButton : Button = findViewById(R.id.buttonHome)

        homeButton.setOnClickListener{

            val homeIntent = Intent(this, MainActivity::class.java)

            startActivity(homeIntent)
        }
        
    }
}