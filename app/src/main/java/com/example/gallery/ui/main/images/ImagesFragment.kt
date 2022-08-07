package com.example.gallery.ui.main.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.databinding.FragmentAlbumsImagesBinding
import com.example.gallery.ui.main.GalleryViewModel
import kotlinx.coroutines.launch

class ImagesFragment: Fragment() {

    private lateinit var binding: FragmentAlbumsImagesBinding
    private val vm: GalleryViewModel by activityViewModels()
    private val columnCount = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsImagesBinding.inflate(inflater)

        with(binding.rvList) {
            layoutManager = GridLayoutManager(context, columnCount)
            adapter = ImagesRecyclerAdapter(listOf()) { pos ->
                //todo: to FullScreen
            }
        }

        lifecycleScope.launch{
            vm.selectedAlbum.collect {
                with(binding.rvList.adapter as ImagesRecyclerAdapter) {
                    val diffUtil = ImagesDiffUtil(values, it.photos)
                    val diffResult = DiffUtil.calculateDiff(diffUtil)

                    values = it.photos
                    diffResult.dispatchUpdatesTo(this)
                }
            }
        }
        return binding.root
    }
}