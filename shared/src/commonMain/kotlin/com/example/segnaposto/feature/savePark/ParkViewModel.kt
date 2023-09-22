package com.example.segnaposto.feature.savePark

import com.example.segnaposto.feature.savePark.model.Park
import com.example.segnaposto.feature.base.BaseViewModel
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import com.example.segnaposto.feature.savePark.model.ParkState
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
            is ParkEvent.OnCheckPermission -> {
                handleOnCheckPermission(
                    permission = event.permission,
                    isGranted = event.isGranted
                )
            }
            is ParkEvent.OnScreenResumed -> {
                handleOnScreenResumed()
            }
            is ParkEvent.OnAddParkClicked -> {
                handleOnAddPark()
            }
            is ParkEvent.OnParkClicked -> {
                _parkState.update { it.copy(test = "Park clicked") }
            }
            is ParkEvent.OnDeleteParkClicked -> {
                _parkState.update { it.copy(test = "Delete park clicked") }
            }

        }
    }

    private fun hasAllPermissionGranted(): Boolean = false

    private fun isGpsActive(): Boolean = true
    private fun handleOnCheckPermission(permission: String, isGranted: Boolean) {
        if(!isGranted) {
            sendUiEvent(
                ParkScreenEvent.ShowPermissionDialog(
                    isVisible = true,
                    permission = permission
                ))
            return
        }

        sendUiEvent(ParkScreenEvent.ShowPermissionDialog(isVisible = false, permission = ""))
        handleOnAddPark()
    }

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

    private fun handleOnAddPark() {

        if (!permissionsUtil.hasAllPermissionGranted()) {
            sendUiEvent(ParkScreenEvent.RequestPermission)
            return
        }

        if(!isGpsActive()) {

        }

        insertPark()
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
    data class OnCheckPermission(val permission: String, val isGranted: Boolean): ParkEvent()
    object OnScreenResumed: ParkEvent()
    object OnAddParkClicked: ParkEvent()
    data class OnParkClicked(val park: Park): ParkEvent()
    data class OnDeleteParkClicked(val park: Park): ParkEvent()
}