package com.example.partnerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FitsAdapter(
    val fits: List<Fit>,
    val context: Context
) : RecyclerView.Adapter<FitsAdapter.MyFitViewHolder>() {

    class MyFitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize views in the MyFitViewHolder
        // For example:
        val clothingImageTop: ImageView = view.findViewById(R.id.clothingImage1)
        val brandNameTop : TextView = view.findViewById(R.id.brandName1)
        val storeNameTop : TextView = view.findViewById(R.id.storeName1)
        val clothingNameTop : TextView = view.findViewById(R.id.clothingName1)
        val clothingImageBottom: ImageView = view.findViewById(R.id.clothingImage2)
        val brandNameBottom : TextView = view.findViewById(R.id.brandName2)
        val storeNameBottom : TextView = view.findViewById(R.id.storeName2)
        val clothingNameBottom : TextView = view.findViewById(R.id.clothingName2)
        val clothingImageAccessory: ImageView = view.findViewById(R.id.clothingImage3)
        val brandNameAccessory : TextView = view.findViewById(R.id.brandName3)
        val storeNameAccessory : TextView = view.findViewById(R.id.storeName3)
        val clothingNameAccessory : TextView = view.findViewById(R.id.clothingName3)
        val clothingImageShoe: ImageView = view.findViewById(R.id.clothingImage4)
        val brandNameShoe : TextView = view.findViewById(R.id.brandName4)
        val storeNameShoe : TextView = view.findViewById(R.id.storeName4)
        val clothingNameShoe : TextView = view.findViewById(R.id.clothingName4)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFitViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fit_cell, parent, false)
        return MyFitViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyFitViewHolder, position: Int) {
        // Bind data to views in the MyFitViewHolder
        // For example:
        // val currentFit = fits[position]
        // holder.fitNameTextView.text = currentFit.name
        // holder.clothingRecyclerView.layoutManager = LinearLayoutManager(context)
        // holder.clothingRecyclerView.adapter = ClothingAdapter(currentFit.clothingItems, context)

        val fit : Fit = fits[position]
        Picasso.get().load(fit.clothingItems[0].imageUrl).into(holder.clothingImageTop)
        //holder.clothingImage.setImageDrawable(clothing.image)
        holder.brandNameTop.text = fit.clothingItems[0].brand
        holder.storeNameTop.text = fit.clothingItems[0].store
        holder.clothingNameTop.text = fit.clothingItems[0].name

        Picasso.get().load(fit.clothingItems[1].imageUrl).into(holder.clothingImageBottom)
        //holder.clothingImage.setImageDrawable(clothing.image)
        holder.brandNameBottom.text = fit.clothingItems[1].brand
        holder.storeNameBottom.text = fit.clothingItems[1].store
        holder.clothingNameBottom.text = fit.clothingItems[1].name

        Picasso.get().load(fit.clothingItems[2].imageUrl).into(holder.clothingImageAccessory)
        //holder.clothingImage.setImageDrawable(clothing.image)
        holder.brandNameAccessory.text = fit.clothingItems[2].brand
        holder.storeNameAccessory.text = fit.clothingItems[2].store
        holder.clothingNameAccessory.text = fit.clothingItems[2].name

        Picasso.get().load(fit.clothingItems[3].imageUrl).into(holder.clothingImageShoe)
        //holder.clothingImage.setImageDrawable(clothing.image)
        holder.brandNameShoe.text = fit.clothingItems[3].brand
        holder.storeNameShoe.text = fit.clothingItems[3].store
        holder.clothingNameShoe.text = fit.clothingItems[3].name
    }

    override fun getItemCount(): Int {
        return fits.size
    }
}
