package com.example.photomemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AddPhotoActivity : AppCompatActivity() {
    private val pickPhotoReuestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

    val openButton: Button = findViewById(R.id.addPhotoMemoOpenButton)
        openButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
                addCategory(Intent.CATEGORY_OPENABLE)
                setType("image/jpeg")
            }
            startActivityForResult(intent, pickPhotoReuestCode)
        }
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickPhotoReuestCode && resultCode == Activity.RESULT_OK){
            data?.data?.let{
                val imageView = findViewById<ImageView>(R.id.addPhotoMemoImageView)
                imageView.setImageURI(it)
            }
        }
    }
}