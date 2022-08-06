package com.example.gallery.ui.main.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gallery.R
import com.example.gallery.databinding.FragmentAlbumsBinding

class AlbumsFragment: Fragment() {

    private lateinit var binding: FragmentAlbumsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsBinding.inflate(layoutInflater)
        binding.textAlbums.text = "Albums Fragment"
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_albumsFragment_to_imagesFragment)
        }

        return binding.root
    }
}