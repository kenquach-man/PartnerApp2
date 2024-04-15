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
    var stability: Long = 0 // Stability of clothing item
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
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
            parcel.writeLong(stability)
        }

        override fun create(parcel: Parcel): Clothing {
            return Clothing(parcel)
        }
    }
}
