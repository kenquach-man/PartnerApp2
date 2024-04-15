package com.example.partnerapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.partnerapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    //val db = Firebase.firestore
    var savedFits = mutableListOf<Fit>()

    fun writeNewUser(userId: String, name: String, email: String) {
        // Convert fits to a list of maps
        val fitMaps = savedFits.map { it.toMap() }

        // Create a User object with username, email, and savedFits
        val user = User(name, email, fitMaps.toMutableList())

        // Convert the User object to a map
        val userValues = user.toMap()

        // Write the user data to the database
        database.child("users").child(userId).updateChildren(userValues)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        database = Firebase.database.reference

        binding.loginredirect.setOnClickListener(){
            val LoginIntent = Intent(this, Login::class.java)
            startActivity(LoginIntent)
        }

        binding.signupbtn.setOnClickListener{
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confirmPass = binding.confirmation.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser

                                user?.let { currentUser ->
                                    // Create a new user in Firebase Realtime Database
                                    writeNewUser(currentUser.uid, username, email)
                                    // Reference the newly created user
                                    /*val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
                                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val userData =
                                                dataSnapshot.getValue(User::class.java)
                                            userData?.let { user ->
                                                // Access the newly created user's fits list
                                                val fitsList = user.fits
                                                userRef.setValue(user)
                                                Log.i("INFO_TAG", "Fits list has been sent to newly created user")
                                                // Now you can use the fitsList as needed
                                            }
                                        }
                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Handle errors here
                                            Log.w(TAG, "loadUser:onCancelled", databaseError.toException())
                                        }
                                    })*/
                                }

                                val LoginIntent = Intent(this, Login::class.java)
                                LoginIntent.putParcelableArrayListExtra("savedFits", ArrayList(savedFits))
                                startActivity(LoginIntent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed. ${task.exception}",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                //updateUI(null)
                            }
                        }
                }else{
                    Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show()
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
                        Log.d(TAG, "signInAnonymously:success")
                        //val user = auth.currentUser
                        //updateUI(user)
                        val homeIntent = Intent(this, MainActivity::class.java)
                        startActivity(homeIntent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.exception)
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