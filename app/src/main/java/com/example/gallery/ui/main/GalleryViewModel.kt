package com.example.gallery.ui.main

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.model.Album
import com.example.gallery.model.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(val app: Application) : AndroidViewModel(app) {

    private val _albums = MutableStateFlow(listOf<Album>())
    val albums = _albums.asStateFlow()
    private val _selectedAlbum = MutableStateFlow(Album("", "", emptyList()))
    val selectedAlbum = _selectedAlbum.asStateFlow()

    fun loadImages() {
        viewModelScope.launch {
            _albums.value = getAllImagesWithAlbum()
        }
    }

    fun selectAlbum(album: Album) {
        _selectedAlbum.value = album
    }


    private fun getAllImagesWithAlbum(): List<Album> {
        val albums = mutableMapOf<String, MutableList<Photo>>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME
//            MediaStore.Images.Media.DATE_TAKEN
        )
        val cursor = app.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )

        if (cursor != null && cursor.count > 0)
            if (cursor.moveToFirst()) {

                val bucketNameColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val imageUriColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val imageIdColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val imageNameColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)


                do {
                    // Получаем поля фото
                    val bucketName = cursor.getString(bucketNameColumn)
                    val imageUri = cursor.getString(imageUriColumn)
                    val imageId = cursor.getString(imageIdColumn)
                    val imageName = cursor.getString(imageNameColumn)

                    val photo = Photo(imageId.toInt(), imageName, bucketName, imageUri)

                    if (!albums.keys.contains(bucketName))
                        albums[bucketName] = mutableListOf()

                    albums[bucketName]!!.add(photo)
                } while (cursor.moveToNext())
            }
        cursor?.close()

        // Формируем результирующий список Album
        val resultList = albums.map { albumEl ->
            val sortedPhotos = albumEl.value.sortedBy { it.name }
            Album(albumEl.key, sortedPhotos.first().uri, sortedPhotos)
        }

        return resultList.sortedBy { it.name }
    }
}