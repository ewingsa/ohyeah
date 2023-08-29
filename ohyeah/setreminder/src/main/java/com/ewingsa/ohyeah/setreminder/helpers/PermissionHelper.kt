package com.ewingsa.ohyeah.setreminder.helpers

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object PermissionHelper {

    fun hasExternalStoragePermission(context: Context?): Boolean {
        return if (SDK_INT < JELLY_BEAN) {
            true
        } else {
            context?.let {
                val permission = if (SDK_INT < TIRAMISU) READ_EXTERNAL_STORAGE else READ_MEDIA_IMAGES
                val permissionCheck = ContextCompat.checkSelfPermission(it, permission)
                permissionCheck == PERMISSION_GRANTED
            } ?: false
        }
    }

    fun requestExternalStoragePermission(activityResultLauncher: ActivityResultLauncher<String>, callback: () -> Unit) {
        if (SDK_INT >= JELLY_BEAN) {
            val permission = if (SDK_INT < TIRAMISU) READ_EXTERNAL_STORAGE else READ_MEDIA_IMAGES
            activityResultLauncher.launch(permission)
        } else {
            callback()
        }
    }
}
