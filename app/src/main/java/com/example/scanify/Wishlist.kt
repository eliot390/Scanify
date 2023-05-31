package com.example.scanify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanify.databinding.ProductRowBinding

class Wishlist : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var productList: ArrayList<ProductModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        dbHelper = DBHelper(this)
        productList = dbHelper.getItems()

        val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WishlistAdapter(productList)
    }

    inner class WishlistAdapter(private val productList: ArrayList<ProductModel>) : RecyclerView.Adapter<WishlistAdapter.CustomViewHolder>() {

        inner class CustomViewHolder(val binding: ProductRowBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ProductRowBinding.inflate(layoutInflater, parent, false)
            return CustomViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val prod = productList[position]

            holder.binding.productTitle.text = prod.title
            holder.binding.storeName.text = prod.name
            holder.binding.storePrice.text = prod.price

            holder.binding.btnSave.setOnClickListener {
                val deleted = dbHelper.deleteItemById(prod.id)
                if (deleted > 0) {
                    productList.remove(prod)
                    notifyItemRemoved(position)
                }
            }
        }

        override fun getItemCount(): Int {
            return productList.size
        }
    }
}
