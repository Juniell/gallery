package com.example.gallery.ui.main.albums

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.ItemFragmentAlbumsBinding
import com.example.gallery.model.Album
import com.example.gallery.model.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AlbumsRecyclerAdapter(
    var values: List<Album>,
    private val onItemClickListener: (position: Int) -> Unit,
    private val loadThumbnail: suspend (photo: Photo) -> Bitmap
) : RecyclerView.Adapter<AlbumsRecyclerAdapter.AlbumsViewHolder>() {

    private val cs = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder =
        AlbumsViewHolder(
            ItemFragmentAlbumsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickListener
        )


    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val album = values[position]
        holder.progressBar.visibility = View.VISIBLE

        cs.launch {
            holder.albumImage.setImageBitmap(loadThumbnail(album.photos.first()))
            holder.progressBar.visibility = View.INVISIBLE
        }

        holder.albumName.text = album.name
        holder.albumCount.text = album.photos.size.toString()
    }

    override fun getItemCount() = values.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        cs.cancel()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    inner class AlbumsViewHolder(
        binding: ItemFragmentAlbumsBinding,
        onItemClickListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        val albumImage = binding.itemAlbumImage
        val albumName = binding.itemAlbumName
        val albumCount = binding.itemAlbumCount
        val progressBar = binding.itemAlbumProgressBar

        init {
            binding.root.setOnClickListener{
                onItemClickListener(absoluteAdapterPosition)
            }
        }
    }
}