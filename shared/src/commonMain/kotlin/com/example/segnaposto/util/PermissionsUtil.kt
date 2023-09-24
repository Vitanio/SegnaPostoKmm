package com.example.segnaposto.util

import com.example.segnaposto.Platform
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent


expect class PermissionsUtil {

    fun getLocationStatus(): PermissionStatus

    /* directly request the permission */
    fun requestPermission(event: (ParkScreenEvent) -> Unit)
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
