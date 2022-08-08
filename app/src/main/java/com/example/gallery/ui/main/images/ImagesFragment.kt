package com.example.gallery.ui.main.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.R
import com.example.gallery.databinding.FragmentAlbumsImagesBinding
import com.example.gallery.ui.main.GalleryViewModel
import kotlinx.coroutines.launch

class ImagesFragment : Fragment() {

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
                vm.selectPhoto(vm.selectedAlbum.value.photos[pos])
                findNavController().navigate(R.id.action_imagesFragment_to_fullscreenFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.selectedAlbum.collect {
                    if (it.isEmpty()) {
                        findNavController().popBackStack()
                        return@collect
                    }

                    with(binding.rvList.adapter as ImagesRecyclerAdapter) {
                        val diffUtil = ImagesDiffUtil(values, it.photos)
                        val diffResult = DiffUtil.calculateDiff(diffUtil)

                        values = it.photos
                        diffResult.dispatchUpdatesTo(this)
                    }
                }
            }
        }
        return binding.root
    }
}