package com.example.segnaposto.feature.savePark

import com.example.segnaposto.feature.savePark.model.Park
import com.example.segnaposto.feature.base.BaseViewModel
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import com.example.segnaposto.feature.savePark.model.ParkState
import com.example.segnaposto.util.PermissionStatus
import com.example.segnaposto.util.PermissionsUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class ParkViewModel(val repository: ParkRepository, val permissionsUtil: PermissionsUtil): BaseViewModel() {

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
            is ParkEvent.OnParkClicked -> {
                _parkState.update { it.copy(test = "Park clicked") }
            }
            is ParkEvent.OnDeleteParkClicked -> {
                _parkState.update { it.copy(test = "Delete park clicked") }
            }

        }
    }

    private fun isGpsActive(): Boolean = true

    private fun sendUiEvent(event: ParkScreenEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleOnScreenResumed() {
        viewModelScope.launch {
            _parkState.update { state ->
                state.copy(parkHistory = repository.getParkHistory())
            }
        }
    }

    private fun handleOnGoToSettings() {
        permissionsUtil.openAppSettings()
    }

    private fun handleOnAddPark() {
        viewModelScope.launch {

            val locationStatus = permissionsUtil.getLocationStatus()

            when(locationStatus){
                PermissionStatus.NotYetRequested -> {
                    permissionsUtil.requestPermission {
                        sendUiEvent(ParkScreenEvent.RequestPermission)
                    }
                }
                PermissionStatus.Denied -> {
                    sendUiEvent(
                        ParkScreenEvent.ShowPermissionDialog(
                            isVisible = true,
                            isRationale = false
                        ))
                }
                PermissionStatus.AndroidDontAskAgain -> {
                    sendUiEvent(
                        ParkScreenEvent.ShowPermissionDialog(
                            isVisible = true,
                            isRationale = true
                        ))
                }
                PermissionStatus.Granted -> {
                    insertPark()
                }
            }
        }
    }

    private fun insertPark() {
        viewModelScope.launch {
            repository.insertPark(createMockedPark())

            _parkState.update { state ->
                state.copy(parkHistory = repository.getParkHistory())
            }
        }
    }

    private fun createMockedPark(): Park {
        return Park(
            title = "Mola di Bari",
            description = "Via Foggia 78",
            latitude = Random.nextInt(0,30).toDouble(),
            longitude = Random.nextInt(0,30).toDouble(),
            date = "Domenica"
        )
    }
}

sealed class ParkEvent {
    object OnScreenResumed: ParkEvent()
    object OnAddParkClicked: ParkEvent()
    object OnGoToSettingsClicked: ParkEvent()
    data class OnParkClicked(val park: Park): ParkEvent()
    data class OnDeleteParkClicked(val park: Park): ParkEvent()
}