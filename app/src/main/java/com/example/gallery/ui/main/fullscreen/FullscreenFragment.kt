package com.example.gallery.ui.main.fullscreen

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gallery.R
import com.example.gallery.databinding.FragmentFullscreenBinding
import com.example.gallery.ui.main.GalleryViewModel
import kotlinx.coroutines.launch

class FullscreenFragment : Fragment() {

    private lateinit var binding: FragmentFullscreenBinding
    private val vm: GalleryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullscreenBinding.inflate(layoutInflater)

        binding.buttonFullscreen.setOnClickListener {
            findNavController().navigate(R.id.action_fullscreenFragment_to_renameDialogFragment)
        }

        lifecycleScope.launch {
            vm.selectedPhoto.collect {
                Glide
                    .with(binding.root)
                    .load(vm.selectedPhoto.value.uri)
                    .into(binding.imageFullscreen)
            }
        }
        hideSystemBars()

        return binding.root
    }

    override fun onDetach() {
        showSystemBars()
        super.onDetach()
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        val controller = WindowCompat.getInsetsController(requireActivity().window, binding.root)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requireActivity().window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    private fun showSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        val controller = WindowCompat.getInsetsController(requireActivity().window, binding.root)
        controller.show(WindowInsetsCompat.Type.systemBars())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requireActivity().window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}