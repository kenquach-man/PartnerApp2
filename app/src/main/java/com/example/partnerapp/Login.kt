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
    var savedFits = mutableListOf<Fit>()

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

                            // Get saved fits from user
                            user?.let { currentUser ->
                                val userRef = FirebaseDatabase.getInstance().getReference("users").child(
                                    currentUser.uid)
                                val savedFitsRef = userRef.child("savedFits")
                                savedFitsRef.get().addOnSuccessListener {
                                    Log.i("firebase", "Got value ${it.value}")
                                    var id = 0
                                    for (fit in it.children) {
                                        val clothingItems = fit.child("clothingItems")
                                        val clothingItemsList = ArrayList<Clothing>()
                                        for (i in clothingItems.children) {
                                            val clothingItemMap = i.getValue<HashMap<String, Any>>()
                                            if (clothingItemMap != null) {
                                                val clothingItem = Clothing()
                                                for (key in clothingItemMap.keys) {
                                                    val value = clothingItemMap[key]
                                                    Log.i("INFO_TAG", "Preparing to find key-value pair")
                                                    when (key) {
                                                        "brand" -> clothingItem.brand = value.toString()
                                                        "id" -> clothingItem.id = value.toString()
                                                        "imageUrl" -> clothingItem.imageUrl = value.toString()
                                                        "name" -> clothingItem.name = value.toString()
                                                        "store" -> clothingItem.store = value.toString()
                                                        "type" -> clothingItem.type = value.toString()
                                                        //"stability" -> if (value != null) {
                                                        //    clothingItem.stability = value.toString().toLong()
                                                        //}
                                                    }
                                                }
                                                clothingItemsList.add(clothingItem)
                                                Log.i("INFO_TAG", "Size of clothingItemsList is ${clothingItemsList.size}")
                                            }
                                        }
                                        val fit = Fit(id.toString(), clothingItemsList)
                                        if (fit != null) {
                                            savedFits.add(fit)
                                            id++
                                            Log.i("TEST_TAG", "Added fit to savedFits for Login")
                                            Log.i("INFO_TAG", "Size of savedFits is ${savedFits.size}")
                                        }
                                    }
                                    val HomeIntent = Intent(this, MainActivity::class.java)
                                    HomeIntent.action = "com.example.partnerapp.ACTION_SEND_SAVED_FITS"
                                    HomeIntent.putParcelableArrayListExtra("savedFits", ArrayList(savedFits))
                                    Log.i("INFO_TAG", "Sending savedFits to Homepage")
                                    startActivity(HomeIntent)
                                }.addOnFailureListener{
                                    Log.e("firebase", "Error getting data", it)
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
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
                    }
                }
        }
    }
}