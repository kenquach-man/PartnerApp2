package com.example.partnerapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.partnerapp.databinding.ActivityLoginBinding
import com.example.partnerapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.values

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.signupredirect.setOnClickListener(){
            val signUpIntent = Intent(this, SignUp::class.java)
            startActivity(signUpIntent)
        }

        binding.loginbtn.setOnClickListener(){
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            //updateUI(user)
                            // Get saved fits from user
                            var savedFits = mutableListOf<Fit>()
                            user?.let { currentUser ->
                                val userRef = FirebaseDatabase.getInstance().getReference().child(
                                    currentUser.uid)
                                val savedFitsRef = userRef.child("savedFits")
                                savedFitsRef.get().addOnSuccessListener {
                                    Log.i("firebase", "Got value ${it.value}")
                                    val id = 0
                                    for (fit in it.children) {
                                        val clothingItems = fit.getValue<List<Clothing>>()
                                        val fit = clothingItems?.let { it1 -> Fit(id, it1) }
                                        if (fit != null) {
                                            Log.i("TEST_TAG", "Added fit to savedFits for Login")
                                            savedFits.add(fit)
                                        }
                                        /*var clothingItems = mutableListOf<Clothing>()
                                        val clothingItemsNode = fit.child("clothingItems")
                                        val clothingItem = Clothing()
                                        for (i in clothingItemsNode.children) {
                                            val values = i.values
                                            for (key in values.keys) {
                                                val value = values[key]
                                                when (key) {
                                                    "brand" -> clothingItem.brand = value
                                                    "id" -> clothingItem.id = value
                                                    "imageUrl" -> clothingItem.imageUrl = value
                                                    "name" -> clothingItem.name = value
                                                    "store" -> clothingItem.store = value
                                                    "type" -> clothingItem.type = value
                                                }
                                            }
                                        }*/
                                    }
                                }.addOnFailureListener{
                                    Log.e("firebase", "Error getting data", it)
                                }
                            }
                            val HomeIntent = Intent(this, MainActivity::class.java)
                            startActivity(HomeIntent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
                        }
                    }
            }else{
                Toast.makeText(this, "Empty Fields are not Allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
        val guestButton : Button = findViewById(R.id.buttonHome2)
        guestButton.setOnClickListener{
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInAnonymously:success")
                        //val user = auth.currentUser
                        //updateUI(user)
                        val homeIntent = Intent(this, MainActivity::class.java)
                        startActivity(homeIntent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInAnonymously:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        //updateUI(null)
                    }
                }
        }
    }
}