package com.example.partnerapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val clothingList = mutableListOf<Clothing>() // holds list of unique clothes
    val AccessoryTypes = arrayOf("Beanies")
    val TopsTypes = arrayOf("Jackets", "Graphics & Tees", "Hoodies", "Button Downs")
    val BottomsTypes = arrayOf("Shorts", "Cargo & Utility Pants")
    val ShoeTypes = arrayOf("Sneakers", "Slides")
    val maleTops = mutableListOf<Clothing>()
    val malePants = mutableListOf<Clothing>()
    val maleShoes = mutableListOf<Clothing>()
    val maleAccessories = mutableListOf<Clothing>()
    var savedFits = mutableListOf<Fit>()

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val savedFitsButton: Button = findViewById(R.id.buttonSave)
        val saveButton: Button = findViewById(R.id.saveButton)
        val feedbackButton: Button = findViewById(R.id.buttonFeedback)
        val loginButton: Button = findViewById(R.id.buttonLogin)
        val generateButton: Button = findViewById(R.id.buttonGenerate)

        auth = FirebaseAuth.getInstance()

        database = Firebase.database.reference

        if (intent.hasExtra("savedFits")) {
            savedFits = intent?.getParcelableArrayListExtra<Fit>("savedFits") as MutableList<Fit>
        }
        savedFitsButton.setOnClickListener {
            val saveIntent = Intent(this, SavedFits::class.java)
            saveIntent.putParcelableArrayListExtra("savedFits", ArrayList(savedFits))
            startActivity(saveIntent)
            Log.i("INFO_TAG", "Size of savedFits is ${savedFits.size}")
        }

        feedbackButton.setOnClickListener {
            val feedbackIntent = Intent(this, Feedback::class.java)
            startActivity(feedbackIntent)
        }
        loginButton.setOnClickListener{
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }

        // LESSON
        // SPLIT FUNCTIONS INTO ACTUAL FUNCTIONS, DON'T CREATE ONE MEGA FUNCTION, MAKES YOUR CODE MORE MODULAR
        // AND EASY TO READ
        // generateButton and saveButton are Asynchronous calls, that means you can call them at any time
        // This is problematic as they need a clothingSet to be generated BEFORE, they are called
        // In order to address this, we can use something called "callbacks", in order to control the "flow" of the code
        // Callbacks allow us to specify what and when something should happen in a code's execution.
        // In this case, we used the completion callback, to ensure that the clothingSet was completely generated, BEFORE
        // being sent to the generateButton and to the saveButton, via sharedSet

        var sharedSet: MutableList<Clothing> = ArrayList()

        generateButton.setOnClickListener {
            generateClothingSet { clothingSet ->
                // Call a function to handle the generated clothing set
                updateUIWithClothingSet(clothingSet)
                sharedSet = clothingSet
            }
            Log.i("TEST_TAG", sharedSet.size.toString())
        }

        saveButton.setOnClickListener {
            // Check if sharedSet is not empty before saving
            if (sharedSet.isNotEmpty()) {
                val fit = Fit(savedFits.size, sharedSet)
                savedFits.add(fit)
                Log.i("INFO_TAG", "Fit has been added to savedFits")
                Log.i("INFO_TAG", "Size of savedFits is ${savedFits.size}")

                val saveIntent = Intent(this, SavedFits::class.java)
                saveIntent.putParcelableArrayListExtra("savedFits", ArrayList(savedFits))
                startActivity(saveIntent)
                Log.i("INFO_TAG", "Intent sent")
                val user = auth.currentUser
                user?.let { currentUser ->
                    // Get current user
                    val userRef =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
                    // Read from the database
                    userRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            val userData = dataSnapshot.getValue(User::class.java)
                            userData?.let { user ->
                                // Access the newly created user's fits list
                                val fitsList = user.fits
                                userRef.setValue(user)
                                Log.i("INFO_TAG", "Fits list has been sent to newly created user")
                                // Now you can use the fitsList as needed
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException())
                        }
                    })
                }
            } else {
                // Handle the case where sharedSet is empty
                Log.e("ERROR_TAG", "Shared set is empty. Unable to save.")
            }
        }

    }

    // Function to check if an item with a specific ID exists in the list
    fun List<Clothing>.hasItemId(id: String): Boolean {
        return any { it.id == id }
    }

    fun generateClothingSet(completion: (MutableList<Clothing>) -> Unit) {
        val clothingSet = mutableListOf<Clothing>() // Clothing set to be displayed
        val root =
            "https://api.apify.com/v2/datasets/kL0MfOcXLwdyohWtd/items?clean=true&format=json&limit=1000"
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, root, null,
            { response ->
                // Inside the success callback of the JSON request
                Log.i("INFO_TAG", "Received JSON response. Parsing data...")
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val name = jsonObject.getString("title")
                        val brand = jsonObject.getString("brand")
                        val store = "Reason Clothing"
                        val type = jsonObject.getString("product_type")
                        val id = jsonObject.getString("id")
                        val imageUrl = jsonObject.getJSONArray("images_urls").getString(0)
                        Log.i("TEST_TAG", imageUrl)

                        val clothing = Clothing(brand, store, name, imageUrl, type, id)
                        // Now you can use the clothing object or pass it to your adapter
                        // Only if it isn't already in the clothingList
                        if(!clothingList.hasItemId(id)) {
                            clothingList.add(clothing)
                            if(TopsTypes.contains(clothing.type)) {
                                maleTops.add(clothing)
                            }
                            if(BottomsTypes.contains(clothing.type)) {
                                malePants.add(clothing)
                            }
                            if(AccessoryTypes.contains(clothing.type)) {
                                maleAccessories.add(clothing)
                            }
                            if(ShoeTypes.contains(clothing.type)) {
                                maleShoes.add(clothing)
                            }
                        }

                        Log.i("INFO_TAG", "Added clothing item: $clothing")
                    }
                    Log.i("INFO_TAG", "Total clothing items: ${clothingList.size}")

                    // Add items to clothing set to be displayed
                    clothingSet.add(maleTops.random())
                    clothingSet.add(malePants.random())
                    clothingSet.add(maleAccessories.random())
                    clothingSet.add(maleShoes.random())

                    Log.i("INFO_TAG", "Clothing Set completed")
                    completion(clothingSet)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                // TODO: Handle error
                Log.i("ERROR_TAG", "Unable to recieve JSON object")
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
        Log.i("TEST_TAG", clothingList.size.toString())
    }

    fun updateUIWithClothingSet(clothingSet: MutableList<Clothing>) {
        // Update the UI with the clothing set
        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ClothingAdapter(clothingSet, this)
    }
}