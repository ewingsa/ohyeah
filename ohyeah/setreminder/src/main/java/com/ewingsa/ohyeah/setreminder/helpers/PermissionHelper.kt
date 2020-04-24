package com.ewingsa.ohyeah.setreminder.helpers

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionHelper {

    private const val EXTERNAL_STORAGE_PERMISSION = 1

    fun hasExternalStoragePermission(context: Context?): Boolean {
        return if (SDK_INT < JELLY_BEAN) {
            true
        } else {
            context?.let {
                val permissionCheck = ContextCompat.checkSelfPermission(
                    it,
                    READ_EXTERNAL_STORAGE
                )
                permissionCheck == PERMISSION_GRANTED
            } ?: false
        }
    }

    fun requestExternalStoragePermission(fragment: Fragment) {
        if (SDK_INT >= JELLY_BEAN) {
            fragment.requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION)
        }
    }

    fun checkExternalStoragePermissionGranted(requestCode: Int, grantResults: IntArray): Boolean {
        return (requestCode == EXTERNAL_STORAGE_PERMISSION && grantResults.isNotEmpty() &&
                grantResults[0] == PERMISSION_GRANTED)
    }
}
