package com.example.gallery.model

import android.net.Uri

data class Photo(
    val id: Int,
    val name: String,
    val albumName: String,
    val uriContent: Uri,
    val uriFile: String
) {
    fun isEmpty() = id < 0 && name.isEmpty() && albumName.isEmpty() && uriContent == Uri.EMPTY && uriFile.isEmpty()

    companion object {
        val emptyPhoto = Photo(-1, "", "", Uri.EMPTY, "")
    }
}