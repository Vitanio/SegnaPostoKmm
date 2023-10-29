package com.example.segnaposto.feature.savePark

import com.example.segnaposto.dialog.LocationPowerStatusTextProvider
import com.example.segnaposto.feature.savePark.model.Park
import com.example.segnaposto.feature.base.BaseViewModel
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import com.example.segnaposto.feature.savePark.model.ParkState
import com.example.segnaposto.util.LocationCoordinates
import com.example.segnaposto.util.PermissionStatus
import com.example.segnaposto.util.LocationManager
import com.example.segnaposto.util.LocationPowerStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParkViewModel(val repository: ParkRepository, val locationManager: LocationManager) :
    BaseViewModel() {

    private val _parkState = MutableStateFlow(ParkState())
    val parkState = _parkState.asStateFlow()

    private val _uiEvent = Channel<ParkScreenEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: ParkEvent) {
        when (event) {
            is ParkEvent.OnScreenResumed -> {
                handleOnScreenResumed()
            }
            is ParkEvent.OnAddParkClicked -> {
                handleOnAddPark()
            }
            is ParkEvent.OnGoToSettingsClicked -> {
                handleOnGoToSettings()
            }
            is ParkEvent.OnStartNavigationClicked -> {
                handleOnStartNavigation(event.park)
            }
            is ParkEvent.OnParkClicked -> {
                _parkState.update { it.copy(test = "Park clicked") }
            }
            is ParkEvent.OnDeleteParkClicked -> {
                handleOnDeletePark(event.park)
            }
            is ParkEvent.OnShowMapClicked -> {
                handleOnShowMapClicked(event.park)
            }
        }
    }

    private fun sendUiEvent(event: ParkScreenEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleOnScreenResumed() {
        viewModelScope.launch {
            _parkState.update { state ->
                state.copy(parkHistory = repository.getParkHistory().asReversed())
            }
        }
    }

    private fun handleOnGoToSettings() {
        locationManager.openAppSettings()
    }

    private fun handleOnStartNavigation(park: Park) {
        locationManager.openMapsForNavigation(park)
    }

    private fun handleOnDeletePark(parkClicked: Park) {
        viewModelScope.launch {
            repository.deleteParkById(parkClicked.id.toLong())

            _parkState.update { state ->
                state.copy(parkHistory = repository.getParkHistory().asReversed())
            }
        }
    }

    private fun handleOnShowMapClicked(parkClicked: Park) {
        viewModelScope.launch {
            sendUiEvent(ParkScreenEvent.ShowMapDialog(park = parkClicked))
        }
    }

    private fun handleOnAddPark() {
        viewModelScope.launch {

            val locationStatus = locationManager.getLocationStatus()
            val locationPowerStatus = locationManager.getLocationPowerStatus()

            when (locationStatus) {
                PermissionStatus.NotYetRequested -> {
                    locationManager.requestPermission {
                        sendUiEvent(ParkScreenEvent.RequestPermission)
                    }
                }
                PermissionStatus.Denied -> {
                    sendUiEvent(
                        ParkScreenEvent.ShowPermissionDialog(
                            isVisible = true,
                            isRationale = false
                        )
                    )
                }
                PermissionStatus.AndroidDontAskAgain -> {
                    sendUiEvent(
                        ParkScreenEvent.ShowPermissionDialog(
                            isVisible = true,
                            isRationale = true
                        )
                    )
                }
                PermissionStatus.Granted -> {

                    when (locationPowerStatus) {
                        LocationPowerStatus.On -> {
                            handleInsertParkOperations()
                        }

                        LocationPowerStatus.Off -> {
                            sendUiEvent(
                                ParkScreenEvent.ShowDialog(textProvider = LocationPowerStatusTextProvider())
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleInsertParkOperations() {

        // TODO: Show spinner

        locationManager.getLocationCoordinates { location ->
            viewModelScope.launch {
                locationManager.stopUpdatingLocation()
                if (location != null) {

                    insertPark(location)
                    addInfoFromCoordinates(location)

                }else{
                    // TODO: Show alert
                }
            }
        }
    }

    private suspend fun insertPark(location: LocationCoordinates) {
        repository.insertPark(id = null, park = parkBuilder(location))

        _parkState.update { state ->
            state.copy(parkHistory = repository.getParkHistory().asReversed())
        }
    }

    private fun addInfoFromCoordinates(location: LocationCoordinates) {
        locationManager.getInfoFromCoordinates(
            latitude = location.latitude,
            longitude = location.longitude
        ) { updatedLocation ->
            viewModelScope.launch {
                repository.insertPark(
                    id = repository.getLatestPark().id.toLong(), parkBuilder(
                        locationCoordinates = updatedLocation
                    )
                )

                _parkState.update { state ->
                    state.copy(parkHistory = repository.getParkHistory().asReversed())
                }
            }
        }
    }

    private fun parkBuilder(locationCoordinates: LocationCoordinates): Park {
        return Park(
            title = locationCoordinates.locationInfo?.locality?:"N/A",
            description = locationCoordinates.locationInfo?.address?:"N/A",
            number = locationCoordinates.locationInfo?.number?:"N/A",
            latitude = locationCoordinates.latitude,
            longitude = locationCoordinates.longitude,
            date = "Domenica"
        )
    }
}

sealed class ParkEvent {
    object OnScreenResumed : ParkEvent()
    object OnAddParkClicked : ParkEvent()
    object OnGoToSettingsClicked : ParkEvent()
    data class OnStartNavigationClicked(val park: Park) : ParkEvent()
    data class OnParkClicked(val park: Park) : ParkEvent()
    data class OnDeleteParkClicked(val park: Park) : ParkEvent()
    data class OnShowMapClicked(val park: Park) : ParkEvent()
}