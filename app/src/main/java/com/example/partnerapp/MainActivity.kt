package com.example.partnerapp

import android.content.Intent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Callback
import org.json.JSONObject
import com.squareup.picasso.Picasso
import kotlinx.coroutines.processNextEventInCurrentThread
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveButton: Button = findViewById(R.id.buttonSave)
        val feedbackButton: Button = findViewById(R.id.buttonFeedback)
        val loginButton: Button = findViewById(R.id.buttonLogin)
        val generateButton: Button = findViewById(R.id.buttonGenerate)

        saveButton.setOnClickListener {
            val saveIntent = Intent(this, SavedFits::class.java)
            startActivity(saveIntent)
        }

        feedbackButton.setOnClickListener {
            val feedbackIntent = Intent(this, Feedback::class.java)
            startActivity(feedbackIntent)
        }
        loginButton.setOnClickListener{
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }

        // Function to check if an item with a specific ID exists in the list
        fun List<Clothing>.hasItemId(id: String): Boolean {
            return any { it.id == id }
        }

        val clothingList = mutableListOf<Clothing>() // holds list of unique clothes
        val AccessoryTypes = arrayOf("Beanies")
        val TopsTypes = arrayOf("Jackets", "Graphics & Tees", "Hoodies", "Button Downs")
        val BottomsTypes = arrayOf("Shorts", "Cargo & Utility Pants")
        val ShoeTypes = arrayOf("Sneakers", "Slides")
        val maleTops = mutableListOf<Clothing>()
        val malePants = mutableListOf<Clothing>()
        val maleShoes = mutableListOf<Clothing>()
        val maleAccessories = mutableListOf<Clothing>()

        generateButton.setOnClickListener {
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

                            runOnUiThread {
                                // Setup RecyclerView after populating clothingList
                                val recyclerView: RecyclerView = findViewById(R.id.recycler)
                                recyclerView.layoutManager = LinearLayoutManager(this)
                                recyclerView.adapter = ClothingAdapter(clothingSet, this)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                },
                { error ->
                    // TODO: Handle error
                    Log.i("ERROR_TAG", "Unable to recieve JSON object")
                }
            )
            Log.i("TEST_TAG", clothingList.size.toString())
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
        }
    }
}