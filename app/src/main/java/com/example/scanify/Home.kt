package com.example.scanify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val actionBar = supportActionBar

        if(actionBar != null){
            actionBar.title = "Home"
        }

        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}