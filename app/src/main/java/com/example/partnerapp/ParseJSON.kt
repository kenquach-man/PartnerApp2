package com.example.partnerapp

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class ParseJSON { // All from ChatGPT

    companion object {
        fun parseJsonFile(jsonFilePath: String): MutableList<Clothing> {
            val clothingItems = mutableListOf<Clothing>()


            try {
                // Read the JSON file
                val inputStream = javaClass.classLoader?.getResourceAsStream(jsonFilePath)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val jsonString = StringBuilder()


                // Read JSON content line by line
                var line: String?
                while (reader.readLine().also { line = it } != null) { // Add line to jsonString, if not null
                    jsonString.append(line)
                }

                println(jsonString)
                // Parse JSON content
                val jsonArray = JSONArray(jsonString.toString()) // Converts big jsonString to array of jsonObjects

                // Extract image URLs
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i) // Whatever json object we're on
                    var clothing = Clothing()
                    clothing.name = item.getString("name")
                    clothing.brand = item.getString("brand")
                    clothing.store = item.getString("store")
                    /* val imageUrl = item.getString("url")
                    val max_height = 500
                    val max_width = 500
                    val imageRequest =  ImageRequest(imageUrl, { response ->
                        clothing.image = BitmapDrawable(response)
                    }, max_height, max_width, null, {
                        println("That didn't work!")
                    })

                    // Add a request (in this example, called imageRequest) to your RequestQueue.
                    MySingleton.getInstance(this.applicationContext).addToRequestQueue(imageRequest) */

                    // Append clothing items to list
                    clothingItems.add(clothing)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return clothingItems
        }
    }

    fun main() {
        val jsonFilePath = "data.json" // Replace with the path to your JSON file
        val imageUrls = parseJsonFile(jsonFilePath)

        // Now you have a list of image URLs, you can proceed to download the images
        for (imageUrl in imageUrls) {
            // Download the image from each URL
            // You can use the ImageLoader class or any other method to download images
            // ImageLoader(imageView).execute(imageUrl)
            println("Downloading image from $imageUrl")
        }
    }
}