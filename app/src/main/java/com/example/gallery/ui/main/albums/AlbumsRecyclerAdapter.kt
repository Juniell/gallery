package com.example.gallery.ui.main.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.databinding.ItemFragmentAlbumsBinding
import com.example.gallery.model.Album

class AlbumsRecyclerAdapter(
    var values: List<Album>,
    private val onItemClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<AlbumsRecyclerAdapter.AlbumsViewHolder>() {

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

        Glide
            .with(holder.root)
            .load(album.uri)
            .into(holder.albumImage)

        holder.albumName.text = album.name
        holder.albumCount.text = album.photos.size.toString()
    }

    override fun getItemCount() = values.size

    inner class AlbumsViewHolder(
        binding: ItemFragmentAlbumsBinding,
        onItemClickListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        val root = binding.root
        val albumImage = binding.itemAlbumImage
        val albumName = binding.itemAlbumName
        val albumCount = binding.itemAlbumCount

        init {
            binding.root.setOnClickListener{
                onItemClickListener(absoluteAdapterPosition)
            }
        }
    }
}