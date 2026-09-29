package com.techquantum.tqdkhata.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object StoragePermissionHelper {

    /**
     * Returns the array of runtime storage permissions required by the current Android OS version.
     * On Android 9 and below (API <= 28): READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE.
     * On Android 10-12 (API 29..32): READ_EXTERNAL_STORAGE.
     * On Android 13+ (API >= 33): Storage Access Framework (SAF) is used, so no legacy dangerous
     * storage permission is required.
     */
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

    /**
     * Checks whether all required storage permissions are currently granted.
     */
    fun hasStoragePermissions(context: Context): Boolean {
        val permissions = getRequiredPermissions()
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Human-friendly message explaining why storage permission is required.
     */
    fun getPermissionRationaleMessage(): String {
        return "Storage permission is required to select, read, and save JSON backup files to your device storage."
    }
}
