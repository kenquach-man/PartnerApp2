package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveButton : Button = findViewById(R.id.buttonSave)
        val feedbackButton : Button = findViewById(R.id.buttonFeedback)

        saveButton.setOnClickListener{

            val saveIntent = Intent(this, SavedFits::class.java)

            startActivity(saveIntent)
        }

        feedbackButton.setOnClickListener{

            val feedbackIntent = Intent(this, Feedback::class.java)

            startActivity(feedbackIntent)
        }
    }




}