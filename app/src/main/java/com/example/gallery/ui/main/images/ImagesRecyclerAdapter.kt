package com.example.gallery.ui.main.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.databinding.ItemFragmentImagesBinding
import com.example.gallery.model.Photo

class ImagesRecyclerAdapter(
    var values: List<Photo>,
    private val onItemClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<ImagesRecyclerAdapter.ImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImagesViewHolder(
            ItemFragmentImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickListener
        )

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val photo = values[position]

        Glide
            .with(holder.root)
            .load(photo.uri)
            .into(holder.image)

        holder.imageName.text = photo.name
    }

    override fun getItemCount(): Int = values.size

    inner class ImagesViewHolder(
        binding: ItemFragmentImagesBinding,
        onItemClickListener: (pos: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        val root = binding.root
        val image = binding.itemPhotoImage
        val imageName = binding.itemPhotoName

        init {
            root.setOnClickListener {
                onItemClickListener(absoluteAdapterPosition)
            }
        }
    }
}