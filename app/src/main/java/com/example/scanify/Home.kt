package com.example.scanify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.content.Intent

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnHistory = findViewById<ImageButton>(R.id.btnHistory)
        val btnScan = findViewById<ImageButton>(R.id.btnScan)
        val btnLogout = findViewById<ImageButton>(R.id.btnLogout)

        btnHistory.setOnClickListener {
            val intent = Intent(this, Scan::class.java)
            startActivity(intent)
        }

        btnScan.setOnClickListener {
            val intent = Intent(this, Scan::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}