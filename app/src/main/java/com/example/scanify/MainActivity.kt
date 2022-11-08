package com.example.scanify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editUsername = findViewById<EditText>(R.id.editUsername)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnReset.setOnClickListener{
            editUsername.setText("")
            editPassword.setText("")
        }

        btnLogin.setOnClickListener{
            val username = editUsername.text
            val password = editPassword.text
            Toast.makeText(this@MainActivity, "Welcome $username!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, home::class.java)
            startActivity(intent)
        }
    }
}