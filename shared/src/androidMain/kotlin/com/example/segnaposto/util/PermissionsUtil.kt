package com.example.segnaposto.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.segnaposto.database.ParkDatabase

actual class PermissionsUtil(private val context: Context) {
    actual fun hasAllPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}