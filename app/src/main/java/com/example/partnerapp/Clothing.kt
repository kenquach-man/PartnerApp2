package com.example.partnerapp

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clothing(
    var brand: String? = "", // Brand of clothing
    var store: String? = "", // Store, piece comes from
    var name: String? = "", // Name of the piece
    var imageUrl: String? = "",
    var type: String? = "", // Type of clothing
    var id: String? = "", // Id of clothing
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    companion object : Parceler<Clothing> {

        override fun Clothing.write(parcel: Parcel, flags: Int) {
            parcel.writeString(brand)
            parcel.writeString(store)
            parcel.writeString(name)
            parcel.writeString(imageUrl)
            parcel.writeString(type)
            parcel.writeString(id)
        }

        override fun create(parcel: Parcel): Clothing {
            return Clothing(parcel)
        }
    }
}