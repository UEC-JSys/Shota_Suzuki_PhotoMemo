package com.example.photomemo.View

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photomemo.*
import com.example.photomemo.Model.Photo
import com.example.photomemo.Model.PhotoRepository
import com.example.photomemo.Model.PhotoRoomDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), PhotoListAdapter.Listener {
    private val addActivityRequestCode = 1
    private lateinit var viewModel: PhotoViewModel
    private val requestExternalStorage = 2
    val adapter = PhotoListAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = PhotoListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
        viewModel.allPhotos.observe(this, Observer { photos ->
            photos?.let { adapter.setPhotos(it)}
        })
        viewModel.allPhotos.observe(this, Observer { photos ->
            photos?.let { adapter.setPhotos(viewModel.getAllThumbs(it)) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddPhotoActivity::class.java)
            startActivityForResult(intent, addActivityRequestCode)
        }
        val permission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestExternalStorage
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != addActivityRequestCode) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(
                        applicationContext,
                        "Photo additions have been cancelled.",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestExternalStorage) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "Must have permission to access external storage.",
                    Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onItemClicked(index: Int) {
        val intent = Intent(this@MainActivity, PhotoDetailActivity::class.java)
        intent.putExtra(PhotoDetailActivity.EXTRA_DATA_INDEX, index)
        startActivity(intent)
    }

}

