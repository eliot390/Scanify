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

    private val userData = MutableLiveData<String>()
    private val passData = MutableLiveData<String>()
    private val validData = MediatorLiveData<Boolean>().apply {
        this.value = false

        addSource(userData) { username ->
            val password = passData.value
            this.value = validateForm(username, password)
        }
        addSource(passData) { password ->
            val username = userData.value
            this.value = validateForm(username, password)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editUsername = findViewById<EditText>(R.id.editUsername)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        editUsername.doOnTextChanged { text, _, _, _ ->
            userData.value = text?.toString()
        }

        editPassword.doOnTextChanged { text, _, _, _ ->
            passData.value = text?.toString()
        }

        validData.observe(this){ isValid ->
            btnLogin.isEnabled = isValid
        }

        btnReset.setOnClickListener{
            editUsername.setText("")
            editPassword.setText("")
        }

        btnLogin.setOnClickListener{
            val username = editUsername.text
            Toast.makeText(this@MainActivity, "Welcome $username!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, home::class.java)
            startActivity(intent)
        }
    }

    private fun validateForm(editUsername: String?, editPassword: String?) : Boolean {
        val validUsername = editUsername != null && editUsername.isNotBlank()
        val validPassword = editPassword != null && editPassword.isNotBlank() && editPassword.length > 5
        return validUsername && validPassword
    }
}