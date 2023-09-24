package com.example.segnaposto.util
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import platform.CoreLocation.CLLocationManager

actual class PermissionsUtil {

    private val locationManager = CLLocationManager()

    actual fun getLocationStatus(): PermissionStatus {
        return if (CLLocationManager.locationServicesEnabled()) {
            return when (locationManager.authorizationStatus) {
                0 -> { // notDetermined,
                    PermissionStatus.NotYetRequested
                }
                1, 2 -> { // restricted, denied
                    PermissionStatus.Denied
                }
                3, 4 -> { // authorizedAlways, authorizedWhenInUse
                    PermissionStatus.Granted
                }
                else -> {
                    PermissionStatus.Denied
                }
            }
        }else{
          PermissionStatus.Denied
        }
    }

    actual fun requestPermission(event: (ParkScreenEvent) -> Unit) {
        locationManager.requestWhenInUseAuthorization()
    }
}