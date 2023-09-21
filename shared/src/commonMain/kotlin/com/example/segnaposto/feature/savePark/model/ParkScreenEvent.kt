package com.example.segnaposto.feature.savePark.model


data class ParkScreenEventState(
    val state: ParkScreenEvent = ParkScreenEvent.InitialState
)
sealed class ParkScreenEvent {
    object InitialState: ParkScreenEvent()
    object RequestPermission: ParkScreenEvent()
    data class ShowPermissionDialog(val text: String) : ParkScreenEvent()
}