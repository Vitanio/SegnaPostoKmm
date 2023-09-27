package com.example.segnaposto.util

import com.example.segnaposto.feature.savePark.model.ParkScreenEvent


expect class LocationManager {

    fun getLocationStatus(): PermissionStatus

    fun getLocationPowerStatus(): LocationPowerStatus

    /* directly request the permission */
    fun requestPermission(event: (ParkScreenEvent) -> Unit)

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
