package com.example.segnaposto.util

import com.example.segnaposto.feature.savePark.model.Park
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent


expect class LocationManager {

    fun getLocationStatus(): PermissionStatus

    fun getLocationPowerStatus(): LocationPowerStatus

    fun getLocationCoordinates(onResultListener: (LocationCoordinates?) -> Unit)

    fun stopUpdatingLocation()

    /* directly request the permission */
    fun requestPermission(event: (ParkScreenEvent) -> Unit)

    fun getInfoFromCoordinates(latitude: Double, longitude: Double, result: (LocationCoordinates) -> Unit)

    fun openMapsForNavigation(park: Park)
    fun openAppSettings()
}

sealed class PermissionStatus {
    object NotYetRequested: PermissionStatus()
    object AndroidDontAskAgain: PermissionStatus()
    object Granted: PermissionStatus()
    object Denied: PermissionStatus()

    override fun toString(): String {
        return when (this) {
            is NotYetRequested -> "NotYetRequested"
            is AndroidDontAskAgain -> "AndroidDontAskAgain"
            is Granted -> "Granted"
            is Denied -> "Denied"
        }
    }
}

sealed class LocationPowerStatus {
    object On: LocationPowerStatus()
    object Off: LocationPowerStatus()
}

data class LocationCoordinates(
    var latitude: Double,
    var longitude: Double,
    val locationInfo: LocationInfo? = null
){
    data class LocationInfo(
        val locality: String?,
        val address: String?,
        val number: String?
    )
}
