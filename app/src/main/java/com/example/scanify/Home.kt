package com.example.scanify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.scanify.R.id.btnLogout

class home : AppCompatActivity() {
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