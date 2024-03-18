package com.example.partnerapp

import android.graphics.drawable.Drawable

data class Clothing (
    var type : String, // Type of clothing
    var store : String, // Store, piece comes from
    var name: String, // Name of the piece
    var image : Drawable,
    )