package com.example.scanify

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.scanify.databinding.ProductRowBinding
import com.squareup.picasso.Picasso

class MainAdapter(private val dataFeed: DataFeed, inflate: ProductRowBinding): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {

    private lateinit var dbHelper: DBHelper

    // # of items to show onscreen
    override fun getItemCount(): Int {
        return dataFeed.product.online_stores.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ProductRowBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val prod = dataFeed.product
        val stores = dataFeed.product.online_stores[position]

        holder.binding.productTitle.text = prod.title
        holder.binding.storeName.text = stores.name
        holder.binding.storePrice.text = stores.price
        holder.binding.storeUrl.text = stores.url

        val productImageView = holder.binding.productImage
        Picasso.get().load(prod.images[0]).into(productImageView)

        holder.binding.btnSave.setOnClickListener {
            val title = prod.title
            val storeName = stores.name
            val price = stores.price

            // Create a DBHelper instance using the context of the button's view
            dbHelper = DBHelper(holder.itemView.context)

            // Create a ProductModel instance using the retrieved data
            val product = ProductModel(name = storeName, price = price, title = title)

            // Insert the product into the database using the DBHelper instance
            dbHelper.insertItem(product)

            // Close the database connection
            dbHelper.close()
        }
    }

    inner class CustomViewHolder(val binding: ProductRowBinding) : RecyclerView.ViewHolder(binding.root)
}