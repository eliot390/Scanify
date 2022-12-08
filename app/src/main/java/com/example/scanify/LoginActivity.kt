package com.example.scanify
/*
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData */

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.scanify.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{login()}
        binding.btnLogout.setOnClickListener{logout()}
    }

    private fun login(){
        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.login_audience, getString(R.string.com_auth0_domain)))
            .start(this, object : Callback<Credentials, AuthenticationException> {
                @SuppressLint("StringFormatInvalid")
                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message, exception.getCode()))
                }

                @SuppressLint("StringFormatInvalid")
                override fun onSuccess(credentials: Credentials) {
                    cachedCredentials = credentials
                    showSnackBar(getString(R.string.login_success_message, credentials.accessToken))
                    updateUI()
                    showUserProfile()
                }
            })
    }

    fun logout(){
        WebAuthProvider
            .logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    updateUI()
                    showSnackBar(getString(R.string.general_failure_with_exception_code, exception.getCode()))
                }

                override fun onSuccess(payload: Void?) {
                    cachedCredentials = null
                    cachedUserProfile = null
                    updateUI()
                }
            })
    }

    private fun showUserProfile(){
        if (cachedCredentials == null){
            return
        }

        val client = AuthenticationAPIClient(account)
        client
            .userInfo(cachedCredentials!!.accessToken)
            .start(object : Callback<UserProfile, AuthenticationException>{
                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code, exception.getCode()))
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    updateUI()
                }
            })
    }

    private fun updateUI(){
        val isLoggedIn = cachedCredentials != null

        binding.textviewTitle.text = if(isLoggedIn){
            getString(R.string.logged_in_title)
        }else{
            getString(R.string.logged_out_title)
        }

        binding.btnLogin.isEnabled = !isLoggedIn
        binding.btnLogout.isEnabled = isLoggedIn
        binding.textviewUserProfile.isVisible = isLoggedIn

        val userName = cachedUserProfile?.name ?: ""
        val userEmail = cachedUserProfile?.email ?: ""
        binding.textviewUserProfile.text = getString(R.string.user_profile, userName, userEmail)

        val intent = Intent(this, Home::class.java)
        Handler().postDelayed({
            startActivity(intent)
        },3000)

    }

    private fun showSnackBar(text: String){
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }
}
/*
class LoginActivity : AppCompatActivity(){

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
        setContentView(R.layout.activity_login)

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
            Toast.makeText(this@LoginActivity, "Welcome $username!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    private fun validateForm(editUsername: String?, editPassword: String?) : Boolean {
        val validUsername = (editUsername != null) && (editUsername.isNotBlank())
        val validPassword = (editPassword != null) && (editPassword.isNotBlank()) && (editPassword.length > 5)
        return validUsername && validPassword
    }
}
*/