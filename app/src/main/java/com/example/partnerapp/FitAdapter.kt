package com.example.partnerapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

private lateinit var auth: FirebaseAuth
private lateinit var database: DatabaseReference
class FitsAdapter(
    val fits: MutableList<Fit>,
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
        val deleteButton: ImageButton = view.findViewById(R.id.delete)
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

        holder.deleteButton.setOnClickListener {
            // Handle click event
            // You can use position to get the position of the item clicked
            auth = FirebaseAuth.getInstance()
            database = Firebase.database.reference
            val position = holder.adapterPosition

            // Ensure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                val fitToRemove = fits[position] // Get the fit to remove from the list

                // Get the current user
                val user = auth.currentUser

                user?.let { currentUser ->
                    // Get reference to the savedFits node for the current user
                    val userRef =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
                    val savedFitsRef = userRef.child("savedFits")

                    // Find the child node with the fit's ID and remove it
                    val fitIdToRemove = fitToRemove.id
                    savedFitsRef.child(fitIdToRemove).removeValue()
                        .addOnSuccessListener {
                            // Remove the fit from the local list as well
                            fits.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, fits.size)
                            Log.d("TAG", "Fit removed successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("TAG", "Error removing fit", exception)
                        }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return fits.size
    }
}
