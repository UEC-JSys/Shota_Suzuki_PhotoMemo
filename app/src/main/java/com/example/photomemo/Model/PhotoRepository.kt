package com.example.photomemo.Model

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import androidx.lifecycle.LiveData
import java.util.*

class PhotoRepository(private val photoDao: PhotoDao) {
    val allPhotos: LiveData<List<Photo>> = photoDao.getPhotos()
    private var allThumbs: MutableMap<Photo, Bitmap?> = mutableMapOf()

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }

    fun getThumbnail(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            return contentResolver.loadThumbnail(uri, Size(100, 100), null)
        } else {
        }
    }

    fun getAllThumbnails(photos: List<Photo>, contentResolver: ContentResolver)
            : Map<Photo, Bitmap?> {
        photos.forEach {
            if (!allThumbs.containsKey(it)) {
                allThumbs[it] = getThumbnail(Uri.parse(it.uri), contentResolver)
            }
        }
        return allThumbs.toMap()


    }

    fun getPhotoInfo(uri: Uri, contentResolver: ContentResolver) : Map<String, String> {
        val info = mutableMapOf<String, String>()
        var cursor: Cursor?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            cursor = contentResolver.query(uri, null, null, null)
        } else {
            cursor = MediaStore.Images.Media.query(
                contentResolver,
                uri,
                null)
        }
        cursor?.let {
            if (it.count != 0) {
                it.moveToFirst()
                info["FileName"] =
                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                val size: Long =
                    it.getLong(it.getColumnIndex(MediaStore.MediaColumns.SIZE))
                info["FileSize"] =
                    String.format(Locale.JAPAN, "%.1fMB", size / 1024.0 / 1024.0)
                it.close()
            }
        }
        return info.toMap()
    }
}
