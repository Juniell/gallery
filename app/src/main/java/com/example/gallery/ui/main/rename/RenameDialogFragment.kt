package com.example.gallery.ui.main.rename

import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.gallery.databinding.DialogFragmentRenameBinding
import com.example.gallery.ui.main.GalleryViewModel
import java.io.File

class RenameDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogFragmentRenameBinding.inflate(layoutInflater)
        val vm: GalleryViewModel by activityViewModels()
        val currFile = File(vm.selectedPhoto.value.uri)

        binding.editTextRename.setText(currFile.nameWithoutExtension)

        binding.buttonAcceptRename.setOnClickListener {
            renameImage(currFile, binding.editTextRename.text.toString())
            dismiss()
        }
        binding.buttonCancelRename.setOnClickListener {
            dismiss()
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