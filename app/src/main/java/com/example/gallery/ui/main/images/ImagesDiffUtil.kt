package com.example.gallery.ui.main.images

import androidx.recyclerview.widget.DiffUtil
import com.example.gallery.model.Photo

class ImagesDiffUtil(
    private val oldList: List<Photo>,
    private val newList: List<Photo>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPhoto = oldList[oldItemPosition]
        val newPhoto = newList[newItemPosition]
        return oldPhoto.id == newPhoto.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPhoto = oldList[oldItemPosition]
        val newPhoto = newList[newItemPosition]
        return oldPhoto.name == newPhoto.name &&
                oldPhoto.albumName == newPhoto.albumName &&
                oldPhoto.uri == newPhoto.uri
    }
}