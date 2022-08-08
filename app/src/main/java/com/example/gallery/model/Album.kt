package com.example.gallery.model

data class Album(
    val name: String,
    val photos: List<Photo>
) {
    fun isEmpty(): Boolean {
        return name.isEmpty() && photos.isEmpty()
    }

    companion object {
        val emptyAlbum = Album("", emptyList())
    }
}