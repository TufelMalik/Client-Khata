package com.techquantum.tqdkhata.utils.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object StoragePermissionHelper {

    fun getRequiredPermissions(): Array<String> {
        return when {
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2 -> arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            else -> emptyArray()
        }
    }

    fun hasStoragePermissions(context: Context): Boolean {
        val permissions = getRequiredPermissions()
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getPermissionRationaleMessage(): String {
        return "Storage permission is required to select, read, and save JSON backup files to your device storage."
    }
}
