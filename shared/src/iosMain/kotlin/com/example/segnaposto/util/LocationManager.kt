package com.example.segnaposto.util

import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLLocationAccuracyBestForNavigation
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.NSObject

actual class LocationManager {

    private val locationManager = CLLocationManager().apply {
        desiredAccuracy = kCLLocationAccuracyBestForNavigation
    }

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
        } else {
            PermissionStatus.Denied
        }
    }

    actual fun getLocationPowerStatus(): LocationPowerStatus {
        return LocationPowerStatus.On // always true
    }

    actual fun requestPermission(event: (ParkScreenEvent) -> Unit) {
        locationManager.requestWhenInUseAuthorization()
    }

    actual fun openAppSettings() {
        val url = NSURL(string = UIApplicationOpenSettingsURLString)

        if (UIApplication.sharedApplication().canOpenURL(url))
            UIApplication.sharedApplication.openURL(url)
    }

    actual fun getLocationCoordinates(onResultListener: (LocationCoordinates?) -> Unit) {
        locationManager.delegate = CLLocationManagerDelegate { coordinates ->
            onResultListener.invoke(
                coordinates
            )
        }
        locationManager.startUpdatingLocation()
    }

    actual fun stopUpdatingLocation() = locationManager.stopUpdatingLocation()

}

class CLLocationManagerDelegate(private val onResultListener: (LocationCoordinates?) -> Unit) :
    NSObject(), CLLocationManagerDelegateProtocol {
    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) =
        notify(didUpdateLocations.last() as? CLLocation)

    override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) =
        notify(manager.location)

    private fun notify(lastLocation: CLLocation?) {
        when {
            lastLocation != null -> {
                lastLocation.coordinate.useContents {
                    onResultListener.invoke(
                        LocationCoordinates(
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                }
            }

            else -> onResultListener.invoke(null)
        }
    }

    private fun addLocationExtraInfoIfPresent(
        location: CLLocation,
        latitude: Double,
        longitude: Double
    ): LocationCoordinates {

        // TODO: Need to be done async ios
        val geocoder = CLGeocoder()

        geocoder.reverseGeocodeLocation(location = location, completionHandler = { placemarks, _ ->
            val placemark = placemarks?.firstOrNull() as? CLPlacemark

            LocationCoordinates(
                latitude = latitude,
                longitude = longitude,
                locationInfo = LocationCoordinates.LocationInfo(
                    locality = placemark?.locality ?: "",
                    address = placemark?.administrativeArea,
                    number = placemark?.country ?: ""
                )
            )
        })
        return LocationCoordinates(
            latitude = latitude,
            longitude = longitude,
            locationInfo = LocationCoordinates.LocationInfo(
                locality = "",
                address = " ",
                number = ""
            )
        )
    }
}



