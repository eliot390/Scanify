package com.example.scanify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
        }
    }

    /*fun onLoginClick(view: View) {
        val txtUsername: TextView = findViewById(R.id.txtUsername)
        val txtPassword: TextView = findViewById(R.id.txtPassword)
        val editUsername: TextView = findViewById(R.id.editUsername)
        val editPassword: TextView = findViewById(R.id.editPassword)

        txtUsername.setText("@string/username" + editUsername.getText().toString())
        txtPassword.setText("@string/password" + editPassword.getText().toString())
    }*/

}