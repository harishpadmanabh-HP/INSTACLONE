package com.harish.instaclone

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

class AuthViewModel (app:Application) : AndroidViewModel(app){

    //var events = MutableLiveData<SignupEvents>()
    val repository = AuthRepository()

    fun getCurrentUser() = repository.getCurrentUser()

    fun registerUser(name: String, email: String, password: String, dpUri: Uri?)
            =repository.registerUser(name, email, password, dpUri,viewModelScope)


    fun getRegistrationStatus()=repository.getAuthStatus()


}

enum class AuthEvents{
    SIGNUP_SUCCESS,
    SIGNUP_FAILED,
    LOGIN_SUCCESS,
    LOGIN_FAILED

}