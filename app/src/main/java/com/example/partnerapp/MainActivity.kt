package com.example.partnerapp

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    //val myWebView: WebView = findViewById(R.id.webview)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.header)
        val imageView = findViewById<ImageView>(R.id.image)

        // Get a RequestQueue
        val queue = MySingleton.getInstance(this.applicationContext).requestQueue
        val url = "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/store-card-40-vision-pro-202401?wid=800&hei=1000&fmt=p-jpg&qlt=95&.v=1705097770616"
        val image = MySingleton.getInstance(this.applicationContext).imageLoader

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                textView.text = "Response is: ${response.substring(0, 500)}"
            },
            Response.ErrorListener { textView.text = "That didn't work!" })

        val max_width = 500
        val max_height = 500

        val imageRequest = ImageRequest(
            url,
            Response.Listener<Bitmap> { response ->
                // Display the image
                imageView.setImageBitmap(response)
            }, max_height, max_width, null, Response.ErrorListener {
                textView.text = "That didn't work!"
            }
        )
        // Add a request (in this example, called imageRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(imageRequest)

        val generateButton : Button = findViewById(R.id.buttonGenerate)

        generateButton.setOnClickListener{
            queue.add(imageRequest)
        }

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


        //myWebView.loadUrl("http://www.example.com")
    }




}