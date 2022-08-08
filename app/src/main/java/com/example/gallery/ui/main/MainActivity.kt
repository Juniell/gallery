package com.example.gallery.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.gallery.R
import com.example.gallery.databinding.ActivityMainBinding

private const val CODE_PERMISSION_EXTERNAL_STORAGE = 100

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val vm: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController

        setContentView(binding.root)
        check()
    }


    private fun check() {
        val rPermissionStatus = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionsList = mutableListOf<String>()

        if (rPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            vm.loadImages()
            vm.registerObserver()
        }
        else
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE)


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val wPermissionStatus = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (wPermissionStatus == PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        // Запрос READ_EXTERNAL_STORAGE и/или WRITE_EXTERNAL_STORAGE
        if (permissionsList.isNotEmpty())
            requestPermissions(permissionsList.toTypedArray(), CODE_PERMISSION_EXTERNAL_STORAGE)

        // Проверка и запрос MANAGE_EXTERNAL_STORAGE для SDK > R
        checkAndRequestManageStorePermission()
    }

    private fun checkAndRequestManageStorePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            return

        if (Environment.isExternalStorageManager())
            return

        val intent = Intent()
        intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION

        val storagePermResLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (Environment.isExternalStorageManager()) {
                //permission granted
                // todo
            } else {
                //permission not granted
                // todo
            }
        }
        storagePermResLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CODE_PERMISSION_EXTERNAL_STORAGE)
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // permission granted
                vm.loadImages()
                vm.registerObserver()
            }
            else {
                // permission denied
                //todo
            }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}