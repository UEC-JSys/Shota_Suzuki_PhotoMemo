package com.example.photomemo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PhotoDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA_INDEX = "jp.co.igel.photomemo.detail.DATA_INDEX"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        val dataIndex = getIntent().getIntExtra(EXTRA_DATA_INDEX, -1)
    }

}