package com.example.gallery.ui.main.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.R
import com.example.gallery.databinding.FragmentAlbumsImagesBinding
import com.example.gallery.ui.main.GalleryViewModel
import kotlinx.coroutines.launch

class AlbumsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumsImagesBinding
    private val vm: GalleryViewModel by activityViewModels()
    private val columnCount = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsImagesBinding.inflate(layoutInflater)

        with(binding.rvList) {
            layoutManager = GridLayoutManager(context, columnCount)
            adapter = AlbumsRecyclerAdapter(listOf()) { pos ->
                vm.selectAlbum(vm.albums.value[pos])
                findNavController().navigate(R.id.action_albumsFragment_to_imagesFragment)
            }
        }

        lifecycleScope.launch {
            vm.albums.collect {
                with(binding.rvList.adapter as AlbumsRecyclerAdapter) {
                    val diffUtil = AlbumsDiffUtil(values, it)
                    val diffResult = DiffUtil.calculateDiff(diffUtil)

                    values = it
                    diffResult.dispatchUpdatesTo(this)
                }
            }
        }

        return binding.root
    }
}