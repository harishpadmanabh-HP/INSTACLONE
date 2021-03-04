package com.harish.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gotoSignup(){
        startActivity(Intent(this,SignUp::class.java))
    }
    fun signupClicked(view: View) {
        gotoSignup()
    }
    fun loginClicked(view: View) {}
}