package com.example.gallery.ui.main.rename

import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gallery.databinding.DialogFragmentRenameBinding
import com.example.gallery.ui.main.GalleryViewModel
import kotlinx.coroutines.launch
import java.io.File

class RenameDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogFragmentRenameBinding.inflate(layoutInflater)
        val vm: GalleryViewModel by activityViewModels()
        val currFile = File(vm.selectedPhoto.value.uriFile)

        binding.editTextRename.setText(currFile.nameWithoutExtension)

        binding.buttonAcceptRename.setOnClickListener {
            renameImage(currFile, binding.editTextRename.text.toString())
            dismiss()
        }
        binding.buttonCancelRename.setOnClickListener {
            dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.selectedPhoto.collect {
                    if (it.isEmpty()) {
                        findNavController().popBackStack()
                        return@collect
                    }
                }
            }
        }

        return binding.root
    }

    private fun renameImage(currFile: File, newName: String) {
        val newFileName = "$newName." + currFile.extension
        val newFile = File(currFile.parentFile, newFileName)

        if (currFile.renameTo(newFile)) {
            MediaScannerConnection.scanFile(
                requireContext(),
                arrayOf(newFile.toString()),
                arrayOf("image/*"),
                null
            )
        }
    }
}