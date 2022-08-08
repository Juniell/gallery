package com.example.gallery.ui.main

import android.app.Application
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.model.Album
import com.example.gallery.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(val app: Application) : AndroidViewModel(app) {

    private val _albums = MutableStateFlow(listOf<Album>())
    val albums = _albums.asStateFlow()
    private val _selectedAlbum = MutableStateFlow(Album.emptyAlbum)
    val selectedAlbum = _selectedAlbum.asStateFlow()
    private val _selectedPhoto = MutableStateFlow(Photo.emptyPhoto)
    var selectedPhoto = _selectedPhoto.asStateFlow()

    fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            _albums.value = getAllImagesWithAlbum()
        }
    }

    fun selectAlbum(album: Album) {
        _selectedAlbum.value = album
    }

    fun selectPhoto(photo: Photo) {
        _selectedPhoto.value = photo
    }

    private fun getAllImagesWithAlbum(): List<Album> {
        val albums = mutableMapOf<String, MutableList<Photo>>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME
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

    fun registerObserver() {
        val handlerThread = HandlerThread("ContentObserverThread")
        handlerThread.start()

        app.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            object : ContentObserver(
                Handler(handlerThread.looper)
            ) {
                override fun onChange(
                    selfChange: Boolean,
                    uris: MutableCollection<Uri>,
                    flags: Int
                ) {
                    println("ИЗМЕНЕНИЯ ПРИШЛИ")
                    val newAlbums = getAllImagesWithAlbum()

                    // Если новый и старый список альбомов совпали, ничего не надо менять
                    if (newAlbums == _albums.value)
                        return
                    // Если списки разные, заменяем текущий список альбомов
                    _albums.value = newAlbums

                    // Если мы ещё не выбирали альбом, дальше ничего не надо менять
                    if (_selectedAlbum.value.isEmpty())
                        return

                    // Если выбранный альбом не изменился, дальше ничего не надо менять
                    if (newAlbums.contains(_selectedAlbum.value))
                        return

                    // Если выбранный альбом изменился, ищем его в новом списке по имени
                    var newSelectedAlbum: Album? = null
                    for (newAlbum in newAlbums) {
                        if (newAlbum.name == _selectedAlbum.value.name) {
                            newSelectedAlbum = newAlbum
                            break
                        }
                    }

                    // Если выбранный альбом не найден, очищаем выбранные альбом и фото
                    if (newSelectedAlbum == null) {
                        _selectedAlbum.value = Album.emptyAlbum
                        _selectedPhoto.value = Photo.emptyPhoto
                        return
                    }

                    // Если нашли выбранный альбом в новом списке, устанавливаем его в качестве выбранного
                    _selectedAlbum.value = newSelectedAlbum

                    // Если мы ещё не выбирали фото, дальше ничего не надо менять
                    if (_selectedPhoto.value.isEmpty())
                        return

                    // Если выбранное фото не изменилось, дальше ничего менять не надо
                    if (newSelectedAlbum.photos.contains(_selectedPhoto.value))
                        return

                    // Если выбранное фото изменилось, ищем его в новом альбоме
                    var newSelectedPhoto: Photo? = null
                    for (newPhoto in newSelectedAlbum.photos) {
                        if (newPhoto.id == _selectedPhoto.value.id) {
                            newSelectedPhoto = newPhoto
                            break
                        }
                    }

                    // Если выбранное фото не найдено, очищаем выбранное фото
                    if (newSelectedPhoto == null) {
                        _selectedPhoto.value = Photo.emptyPhoto
                        return
                    }

                    // Если нашли выбранное фото в новом альбоме, устанавливаем его в качестве выбранного
                    _selectedPhoto.value = newSelectedPhoto

                    super.onChange(selfChange, uris, flags)
                }

            }
        )
    }
}