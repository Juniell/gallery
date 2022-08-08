package com.example.gallery.ui.main.images

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.ItemFragmentImagesBinding
import com.example.gallery.model.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ImagesRecyclerAdapter(
    var values: List<Photo>,
    private val onItemClickListener: (position: Int) -> Unit,
    private val onItemLongClickListener: (position: Int) -> Boolean,
    private val loadThumbnail: suspend (photo: Photo) -> Bitmap
) : RecyclerView.Adapter<ImagesRecyclerAdapter.ImagesViewHolder>() {

    private val cs = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImagesViewHolder(
            ItemFragmentImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickListener,
            onItemLongClickListener
        )

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val photo = values[position]
        holder.progressBar.visibility = View.VISIBLE

        cs.launch {
            holder.image.setImageBitmap(loadThumbnail(photo))
            holder.progressBar.visibility = View.INVISIBLE
        }

        holder.imageName.text = photo.name
    }

    override fun getItemCount(): Int = values.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        cs.cancel()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    inner class ImagesViewHolder(
        binding: ItemFragmentImagesBinding,
        onItemClickListener: (pos: Int) -> Unit,
        onItemLongClickListener: (position: Int) -> Boolean,
    ) : RecyclerView.ViewHolder(binding.root) {

        //        val root = binding.root
        val image = binding.itemPhotoImage
        val imageName = binding.itemPhotoName
        val progressBar = binding.itemPhotoProgressBar

        init {
            binding.root.setOnClickListener {
                onItemClickListener(absoluteAdapterPosition)
            }
            binding.root.setOnLongClickListener {
                onItemLongClickListener(absoluteAdapterPosition)
            }
        }
    }
}