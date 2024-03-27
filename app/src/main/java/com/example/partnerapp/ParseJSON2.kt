package com.example.partnerapp

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest

class ParseJSON2 {

    companion object {
        fun parseJsonFile(url: String): MutableList<Clothing> {
            val clothingList = mutableListOf<Clothing>()
            val url = "https://api.apify.com/v2/datasets/h57KXJrLOEFque3Eo/items?clean=true&format=json&omit=extra"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    Log.i("JSON_TAG", response.toString())
                    try {
                        for (i in 0 until response.length()) {
                            val jsonobject = response.getJSONObject(i)
                            val clothingID = jsonobject.getInt("id")
                            val name = jsonobject.getString("name")
                            val brand = jsonobject.getString("brand")
                            val store = jsonobject.getString("store")
                            val clothing = Clothing(brand, store, name)
                            clothingList.add(clothing)
                            /*Log.i("JSON_TAG", "Recieved ID : : " + clothingID.toString())
                            Log.i("JSON_TAG", "Recieved name : : " + name)
                            Log.i("JSON_TAG", "Recieved brand : : " + brand)
                            Log.i("JSON_TAG", "Recieved store : : " + store)
                            Log.i("JSON_TAG", "-----------------------------------")*/
                        }
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    // TODO: Handle error
                    Log.i("ERROR_TAG", "Unable to recieve JSON object")
                }
            )

            // Access the RequestQueue through your singleton class.
            //MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)

            return clothingList
        }
    }
}