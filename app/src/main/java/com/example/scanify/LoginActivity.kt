package com.example.scanify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message, error.getCode()))
                }

                @SuppressLint("StringFormatInvalid")
                override fun onSuccess(result: Credentials) {
                    cachedCredentials = result
                    //showSnackBar(getString(R.string.login_success_message, result.accessToken))
                    updateUI()
                    showUserProfile()
                }
            })
    }

    private fun logout(){
        WebAuthProvider
            .logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    updateUI()
                    showSnackBar(getString(R.string.general_failure_with_exception_code, error.getCode()))
                }

                override fun onSuccess(result: Void?) {
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
                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code, error.getCode()))
                }

                override fun onSuccess(result: UserProfile) {
                    cachedUserProfile = result
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
        binding.textviewUserProfile.text = getString(R.string.user_profile, userName)

        val intent = Intent(this, Home::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
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