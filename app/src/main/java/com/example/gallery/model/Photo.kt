package com.example.gallery.model

data class Photo(
    val id: Int,
    val name: String,
    val albumName: String,
    val uri: String
) {
    fun isEmpty() = id < 0 && name.isEmpty() && albumName.isEmpty() && uri.isEmpty()

    companion object {
        val emptyPhoto = Photo(-1, "", "", "")
    }
}