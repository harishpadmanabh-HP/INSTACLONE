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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream

class SignUp : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE =100
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        profile_image.setOnClickListener {
            takePicture()
        }
    }

    fun signupClicked(view: View) {}

    fun takePicture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {picIntent->
            picIntent.resolveActivity(packageManager).also {
                startActivityForResult(picIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uplaodAndSaveImageUrl(imageBitmap)
        }
    }

    private fun uplaodAndSaveImageUrl(imageBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance().reference.child("pics/")

        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val upload = storageRef.putBytes(baos.toByteArray())
        upload.addOnCompleteListener{uploadTask->

            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener {downloadURLTask->
                    downloadURLTask.result?.let {
                        imageUri=it
                        Log.e("dp uri",it.toString())
                        profile_image.setImageBitmap(imageBitmap)
                    }

                }
            }

        }
    }
}