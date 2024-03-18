package com.example.partnerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClothingAdapter(
    val exampleList : List<Clothing>,
    val context : Context
) : RecyclerView.Adapter<ClothingAdapter.MyClothingViewHolder>() {

    class MyClothingViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val clothingImage: ImageView = view.findViewById(R.id.clothingImage)
        val typeName : TextView = view.findViewById(R.id.typeName)
        val storeName : TextView = view.findViewById(R.id.storeName)
        val clothingName : TextView = view.findViewById(R.id.clothingName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyClothingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.clothing_cell, parent, false)
        return MyClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyClothingViewHolder, position: Int) {
        val clothing : Clothing = exampleList[position]
        holder.clothingImage.setImageDrawable(clothing.image)
        holder.typeName.text = clothing.type
        holder.storeName.text = clothing.store
        holder.clothingName.text = clothing.name

    }

    override fun getItemCount(): Int {
        return exampleList.size
    }
}