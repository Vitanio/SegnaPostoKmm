package com.example.segnaposto.feature.savePark

import com.example.segnaposto.feature.savePark.model.Park
import com.example.segnaposto.feature.base.BaseViewModel
import com.example.segnaposto.feature.savePark.model.ParkScreenEvent
import com.example.segnaposto.feature.savePark.model.ParkScreenEventState
import com.example.segnaposto.feature.savePark.model.ParkState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class ParkViewModel(val repository: ParkRepository): BaseViewModel() {

    private val _parkState = MutableStateFlow(ParkState())
    val parkState = _parkState.asStateFlow()

    private val _uiEvent = MutableStateFlow(ParkScreenEventState())
    val uiEvent = _uiEvent.asStateFlow()

    fun onEvent(event: ParkEvent) {
        when (event) {
            is ParkEvent.OnCheckPermission -> {
                handleOnCheckPermission(
                    fineLocationGranted = event.fineLocationGranted,
                    coarseLocationGranted = event.coarseLocationGranted
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
    private fun handleOnCheckPermission(fineLocationGranted: Boolean, coarseLocationGranted: Boolean) {
        if(fineLocationGranted && coarseLocationGranted) {
            insertPark()
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

        if (!hasAllPermissionGranted()) {
            _uiEvent.update { state ->
                state.copy(state = ParkScreenEvent.RequestPermission)
            }
            return
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
    data class OnCheckPermission(val fineLocationGranted: Boolean, val coarseLocationGranted: Boolean): ParkEvent()
    object OnScreenResumed: ParkEvent()
    object OnAddParkClicked: ParkEvent()
    data class OnParkClicked(val park: Park): ParkEvent()
    data class OnDeleteParkClicked(val park: Park): ParkEvent()
}