package com.example.segnaposto.feature.savePark.model

import com.example.segnaposto.dialog.BaseTextProvider


data class ParkScreenEventState(
    val state: ParkScreenEvent = ParkScreenEvent.InitialState
)
sealed class ParkScreenEvent {
    object InitialState: ParkScreenEvent()
    object RequestPermission: ParkScreenEvent()
    data class ShowPermissionDialog(val isVisible: Boolean, val isRationale: Boolean) : ParkScreenEvent()

    data class ShowDialog(val textProvider: BaseTextProvider) : ParkScreenEvent()
}