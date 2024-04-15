package com.example.partnerapp

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fit(
    val id: String, // Unique identifier for the fit
    val clothingItems: List<Clothing>
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "clothingItems" to clothingItems
        )
    }
}
