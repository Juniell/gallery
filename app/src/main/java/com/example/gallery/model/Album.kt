package com.example.gallery.model

data class Album(
    val name: String,
    val uri: String,
    val photos: List<Photo>
)