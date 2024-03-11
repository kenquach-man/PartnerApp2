package com.example.partnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    //val myWebView: WebView = findViewById(R.id.webview)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.header)
        // ...

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://www.nordstrom.com/?utm_content=47251460380&utm_term=kwd-13484996&utm_channel=low_nd_psb_brand&utm_source=google&utm_campaign=932956788&adpos=&creative=573001780769&device=c&matchtype=e&network=g&acctid=21700000001689570&dskeywordid=43700049869797827&lid=43700049869797827&ds_s_kwgid=58700005467362665&locationid=9061201&targetid=kwd-13484996&campaignid=932956788&adgroupid=47251460380&gad_source=1&gclid=CjwKCAjw17qvBhBrEiwA1rU9w9XL5GvabA3zikubgQ2XAqSYZClN7rqx331e78JD4zB3BDNzh6nTkBoCeQYQAvD_BwE&gclsrc=aw.ds"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                textView.text = "Response is: ${response.substring(0, 500)}"
            },
            Response.ErrorListener { textView.text = "That didn't work!" })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)


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