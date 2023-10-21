package com.example.segnaposto.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.segnaposto.feature.savePark.ParkEvent
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnSuccessListener
import java.util.Locale

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

    @SuppressLint("MissingPermission")
    actual fun getLocationCoordinates(onResultListener: (LocationCoordinates?) -> Unit) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        var locationCoordinates: LocationCoordinates? = null

        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    locationCoordinates = LocationCoordinates(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                }

                onResultListener.invoke(locationCoordinates)
        }
    }

    actual fun getInfoFromCoordinates(latitude: Double, longitude: Double, result: (LocationCoordinates) -> Unit) {
        // TODO: Need to be done async android
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        result.invoke(
            LocationCoordinates(
                latitude = latitude,
                longitude = longitude,
                locationInfo = LocationCoordinates.LocationInfo(
                    locality = addresses?.first()?.locality,
                    address = addresses?.first()?.thoroughfare,
                    number = addresses?.first()?.subThoroughfare
                )
            )
        )
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

    private fun getLocationMode(context: Context): Int {
        var locationMode = Settings.Secure.LOCATION_MODE_OFF
        try {
            locationMode =
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (_: Settings.SettingNotFoundException) {}
        return locationMode
    }


    actual fun stopUpdatingLocation(){
        // ios specific, nothing to do here
    }


}