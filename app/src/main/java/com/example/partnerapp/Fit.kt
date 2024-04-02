package com.example.partnerapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fit(
    val id: Int, // Unique identifier for the fit
    val clothingItems: List<Clothing>
) : Parcelable
