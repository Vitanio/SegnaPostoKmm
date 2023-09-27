package com.example.segnaposto.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.segnaposto.feature.savePark.ParkEvent
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent


actual class LocationManager(private val context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("PermissionsUtil", Context.MODE_PRIVATE)

    actual fun getLocationStatus(): PermissionStatus {

        val previousLocationStatus =
            sharedPreferences.getString("previousLocationStatus", PermissionStatus.NotYetRequested.toString())

        var returnValue: PermissionStatus = PermissionStatus.Granted

        when {
            hasToGrantPermission() && (previousLocationStatus == PermissionStatus.NotYetRequested.toString()) -> {
                returnValue = PermissionStatus.NotYetRequested // first time asking
            }
            hasToGrantPermission() -> {
                returnValue = PermissionStatus.AndroidDontAskAgain // permission denied multiple times
            }
            hasDeniedPermission() -> {
                returnValue = PermissionStatus.Denied // permission already denied
            }
        }

        sharedPreferences.edit().putString("previousLocationStatus", returnValue.toString()).apply()

        return returnValue
    }

    private fun hasToGrantPermission(): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && hasDeniedPermission()
    }

    private fun hasDeniedPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
    }

    actual fun requestPermission(event: (ParkScreenEvent) -> Unit) {
        event.invoke(ParkScreenEvent.RequestPermission)
    }

    actual fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also {
            context.startActivity(it)
        }
    }

    actual fun getLocationPowerStatus(): LocationPowerStatus {
        return if(getLocationMode(context) != Settings.Secure.LOCATION_MODE_OFF) LocationPowerStatus.On else LocationPowerStatus.Off
    }

    private fun getLocationMode(context: Context): Int {
        var locationMode = Settings.Secure.LOCATION_MODE_OFF
        try {
            locationMode =
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (_: Settings.SettingNotFoundException) {}
        return locationMode
    }
}