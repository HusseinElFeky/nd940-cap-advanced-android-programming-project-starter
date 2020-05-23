package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionsHelper {

    const val REQUEST_FINE_LOCATION_PERMISSION = 1000

    const val PERMISSION_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION

    fun isPermissionGranted(context: Context, permission: String): Boolean {
        val deviceVersion = Build.VERSION.SDK_INT
        return if (deviceVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestPermission(fragment: Fragment, permission: String, requestCode: Int) {
        requestPermissions(fragment, arrayOf(permission), requestCode)
    }

    fun requestPermissions(fragment: Fragment, permissions: Array<String>, requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }
}
