package com.example.segnaposto.util
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

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

    actual fun openAppSettings() {
        val url = NSURL(string = UIApplicationOpenSettingsURLString)

        if(UIApplication.sharedApplication().canOpenURL(url))
            UIApplication.sharedApplication.openURL(url)
    }
}