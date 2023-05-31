package com.example.scanify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanify.databinding.ActivityManualScanBinding
import com.example.scanify.databinding.ProductRowBinding
import com.google.gson.GsonBuilder
//import kotlinx.android.synthetic.main.activity_manual_scan.*
import okhttp3.*
import java.io.IOException

class ManualScan : AppCompatActivity() {
    private lateinit var binding: ActivityManualScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManualScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        fetchJson()
    }

    fun fetchJson() {

        val barcode = intent.getStringExtra("barcode")
        val request = Request.Builder()
            .url("https://barcodes1.p.rapidapi.com/?query=$barcode")
            .get()
            .addHeader("X-RapidAPI-Key", "f3406fb6e0msh728732180988c91p113947jsnf64913b6134e")
            .addHeader("X-RapidAPI-Host", "barcodes1.p.rapidapi.com")
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val dataFeed = gson.fromJson(body, DataFeed::class.java)

                runOnUiThread{
                    val recyclerView = binding.recyclerView
                    recyclerView.adapter = MainAdapter(dataFeed, ProductRowBinding.inflate(layoutInflater))
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}

class DataFeed(val product: Product)
class Product(val title: String, val images: List<String>, val online_stores: List<Stores>)
class Stores(val name: String, val price: String, val url: String)