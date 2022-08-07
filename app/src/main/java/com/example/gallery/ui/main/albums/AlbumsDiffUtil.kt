package com.example.gallery.ui.main.albums

import androidx.recyclerview.widget.DiffUtil
import com.example.gallery.model.Album

class AlbumsDiffUtil(
    private val oldList: List<Album>,
    private val newList: List<Album>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldAlbum = oldList[oldItemPosition]
        val newAlbum = newList[newItemPosition]
        return oldAlbum.name == newAlbum.name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldAlbum = oldList[oldItemPosition]
        val newAlbum = newList[newItemPosition]
        return oldAlbum.uri == newAlbum.uri &&
                oldAlbum.photos == newAlbum.photos
    }
}