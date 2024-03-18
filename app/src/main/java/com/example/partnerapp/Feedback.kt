package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val homeButton : Button = findViewById(R.id.buttonHome1)

        homeButton.setOnClickListener{

            val homeIntent = Intent(this, MainActivity::class.java)

            startActivity(homeIntent)
        }
    }
}