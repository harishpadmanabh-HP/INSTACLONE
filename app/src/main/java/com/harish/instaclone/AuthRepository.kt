package com.harish.instaclone

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope

class AuthRepository {

    var authEvents = MutableLiveData<AuthEvents>()

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getAuthStatus() = authEvents


    fun registerUser(name: String, email: String, password: String, dpUri: Uri?,scope: CoroutineScope){

       val auth =      FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { registerationTask ->
                    if (registerationTask.isSuccessful) {
                        //events.value = SignupEvents.SIGNUP_SUCCESS
                        //createUserinDb(name, email, password, dpUri)
                        authEvents.value = AuthEvents.SIGNUP_SUCCESS
                    } else {
                        authEvents.value = AuthEvents.SIGNUP_FAILED
                    }

                }









    }

    private fun createUserinDb(name: String, email: String, password: String, dpUri: Uri) {

        var status = false
        val user = FirebaseAuth.getInstance().currentUser
        val userDataMap = HashMap<String, String>()
        userDataMap.apply {
            put("name", name)
            put("email", email)
            put("pswd", password)
           // put("dpUri", dpUri.toString())

            val dbRef = FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
                FirebaseDatabase.getInstance().reference.child(USERS_TABLE).child(userId).also {
                    it.setValue(userDataMap).addOnCompleteListener { addUserTask ->
                        if(addUserTask.isSuccessful)
                            authEvents.value = AuthEvents.SIGNUP_SUCCESS
                        else
                            authEvents.value = AuthEvents.SIGNUP_FAILED
                    }
                }
            }
        }
    }


}