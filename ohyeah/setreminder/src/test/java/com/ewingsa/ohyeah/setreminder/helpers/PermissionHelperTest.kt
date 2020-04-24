package com.ewingsa.ohyeah.setreminder.helpers

import android.content.pm.PackageManager.PERMISSION_GRANTED
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionHelperTest {

    @Test
    fun testCheckExternalStoragePermissionGranted_granted() {
        val grantResults = intArrayOf(PERMISSION_GRANTED)
        assertTrue(PermissionHelper.checkExternalStoragePermissionGranted(EXTERNAL_STORAGE_PERMISSION, grantResults))
    }

    @Test
    fun testCheckExternalStoragePermissionGranted_notGranted() {
        val grantResults = intArrayOf()
        assertFalse(PermissionHelper.checkExternalStoragePermissionGranted(EXTERNAL_STORAGE_PERMISSION, grantResults))
    }

    private companion object {
        const val EXTERNAL_STORAGE_PERMISSION = 1
    }
}
