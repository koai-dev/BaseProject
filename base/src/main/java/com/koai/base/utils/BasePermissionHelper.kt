package com.koai.base.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


abstract class BasePermissionHelper {
    abstract fun permissions(): Array<String>

    companion object {
        private const val PERMISSION_ALL = 1001
    }

    open fun hasPermissions(context: Context): Boolean =
        permissions().all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    open fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, permissions(),
            PERMISSION_ALL
        )
    }
}

object PermissionHelper : BasePermissionHelper() {
    override fun permissions(): Array<String> {
        return arrayOf(Manifest.permission.CAMERA)
    }
}
