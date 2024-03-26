package com.example.partnerapp

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import kotlinx.serialization.Serializable

@Serializable
data class Clothing(
    var brand: String = "", // Brand of clothing
    var store: String = "", // Store, piece comes from
    var name: String = "", // Name of the piece
    var imageUrl: String = "",
    var type: String = "", // Type of clothing
    var id: String = "", // Id of clothing
    ) {
}