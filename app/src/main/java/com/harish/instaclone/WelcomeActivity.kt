package com.harish.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initViewModel()
    }

    private fun checkForUser() {
        val currentUser = viewModel.getCurrentUser()
        if (currentUser != null) {
            startActivity(Intent(this, Home::class.java))
            finish()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        checkForUser()

    }

    fun gotoLogin(view: View) {
        startActivity(Intent(this, Login::class.java))
        finish()

    }

    fun gotoSignup(view: View) {
        startActivity(Intent(this, SignUp::class.java))
        finish()
    }

}