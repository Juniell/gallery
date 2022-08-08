package com.example.gallery.model

data class Album(
    val name: String,
    val uri: String,
    val photos: List<Photo>
) {
    fun isEmpty(): Boolean {
        return name.isEmpty() && uri.isEmpty() && photos.isEmpty()
    }

    companion object {
        val emptyAlbum = Album("", "", emptyList())
    }
}