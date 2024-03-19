package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val homeButton : Button = findViewById(R.id.buttonHome2)

        homeButton.setOnClickListener{

            val homeIntent = Intent(this, MainActivity::class.java)

            startActivity(homeIntent)
        }
    }
}