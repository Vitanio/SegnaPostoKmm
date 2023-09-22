package com.example.segnaposto.util

import platform.CoreLocation.CLLocationManager

actual class PermissionsUtil {
    actual fun hasAllPermissionGranted(): Boolean {

        val locationManager = CLLocationManager()

        if (CLLocationManager.locationServicesEnabled()) {
            return when (locationManager.authorizationStatus) {
                0, 1, 2 -> { // notDetermined, restricted, denied
                    false
                }

                3, 4 -> { // authorizedAlways, authorizedWhenInUse
                    true
                }
                else -> {
                    false
                }
            }
        }else{
            return false
        }
    }
}