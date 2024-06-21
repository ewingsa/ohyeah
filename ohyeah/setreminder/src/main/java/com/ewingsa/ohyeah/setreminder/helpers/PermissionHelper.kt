package com.ewingsa.ohyeah.setreminder.helpers

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
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
            context?.let { nonNullContext ->
                when {
                    SDK_INT > TIRAMISU -> listOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
                    SDK_INT == TIRAMISU -> listOf(READ_MEDIA_IMAGES)
                    else -> listOf(READ_EXTERNAL_STORAGE)
                }.all { ContextCompat.checkSelfPermission(nonNullContext, it) == PERMISSION_GRANTED }
            } ?: false
        }
    }

    fun requestExternalStoragePermission(activityResultLauncher: ActivityResultLauncher<Array<String>>, callback: () -> Unit) {
        if (SDK_INT >= JELLY_BEAN) {
            val permissions = when {
                SDK_INT > TIRAMISU -> arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
                SDK_INT == TIRAMISU -> arrayOf(READ_MEDIA_IMAGES)
                else -> arrayOf(READ_EXTERNAL_STORAGE)
            }
            activityResultLauncher.launch(permissions)
        } else {
            callback()
        }
    }
}
