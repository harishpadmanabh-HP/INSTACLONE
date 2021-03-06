package com.harish.instaclone

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream

class SignUp : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViewModel()
        setupObservers()
        profile_image.setOnClickListener {
            takePicture()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun setupObservers(){
        viewModel.apply {
            getRegistrationStatus().observe(this@SignUp, Observer {
                when(it){
                    AuthEvents.SIGNUP_SUCCESS->{
                        startActivity(Intent(this@SignUp,Home::class.java))
                    }
                    AuthEvents.SIGNUP_FAILED->{
                        Toast.makeText(this@SignUp, "SignUp Failed", Toast.LENGTH_SHORT).show()
                    }

                }
            })
//            events.observe(this@SignUp, Observer {
//                when(it){
//                    SignupEvents.SIGNUP_SUCCESS->{
//                        startActivity(Intent(this@SignUp,Home::class.java))
//                    }
//                    SignupEvents.SIGNUP_FAILED->{
//                        Toast.makeText(this@SignUp, "SignUp Failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
        }
    }

    fun signupClicked(view: View) {

     //   if (::imageUri.isInitialized) {
            if (et_name.text.isNotEmpty() &&
                et_email.text.isNotEmpty() &&
                et_pass.text.isNotEmpty()
            ){
                viewModel.registerUser(et_name.text.toString(),et_email.text.toString(),et_pass.text.toString(),null)
            }


//        } else {
//            Toast.makeText(this, "Please upload your image", Toast.LENGTH_SHORT).show()
//        }
    }

    fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { picIntent ->
            picIntent.resolveActivity(packageManager).also {
                startActivityForResult(picIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uplaodAndSaveImageUrl(imageBitmap)
        }
    }

    private fun uplaodAndSaveImageUrl(imageBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance().reference.child("pics/")

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val upload = storageRef.putBytes(baos.toByteArray())
        upload.addOnCompleteListener { uploadTask ->

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { downloadURLTask ->
                    downloadURLTask.result?.let {
                        imageUri = it
                        Log.e("dp uri", it.toString())
                        profile_image.setImageBitmap(imageBitmap)
                    }

                }
            }

        }
    }


}