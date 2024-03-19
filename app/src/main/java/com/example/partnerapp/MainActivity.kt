package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveButton: Button = findViewById(R.id.buttonSave)
        val feedbackButton: Button = findViewById(R.id.buttonFeedback)
        val loginButton: Button = findViewById(R.id.buttonLogin)

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
        // Get a RequestQueue
        val queue = MySingleton.getInstance(this.applicationContext).requestQueue
        val clothingList = mutableListOf<Clothing>()
        val url =
            "https://api.apify.com/v2/datasets/h57KXJrLOEFque3Eo/items?clean=true&format=json&omit=extra"
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                // Inside the success callback of the JSON request
                Log.i("INFO_TAG", "Received JSON response. Parsing data...")
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val brand = jsonObject.getString("brand")
                        val store = "Nordstrom"
                        val clothing = Clothing(brand, store, name)
                        clothingList.add(clothing)
                        Log.i("INFO_TAG", "Added clothing item: $clothing")
                    }
                    Log.i("INFO_TAG", "Total clothing items: ${clothingList.size}")

                    // Setup RecyclerView after populating clothingList
                    val recyclerView: RecyclerView = findViewById(R.id.recycler)
                    recyclerView.layoutManager = GridLayoutManager(this, 1)
                    recyclerView.adapter = ClothingAdapter(clothingList, this)
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