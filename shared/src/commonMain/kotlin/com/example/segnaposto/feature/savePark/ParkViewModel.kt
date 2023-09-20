package com.example.segnaposto.feature.savePark

import com.example.segnaposto.feature.savePark.model.Park
import com.example.segnaposto.feature.base.BaseViewModel
import com.example.segnaposto.feature.savePark.model.ParkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class ParkViewModel(val repository: ParkRepository): BaseViewModel() {

    private val _uiEvent = MutableStateFlow(ParkState())
    val uiEvent = _uiEvent.asStateFlow()

    fun onEvent(event: ParkEvent) {
        when (event) {
            is ParkEvent.onScreenResumed -> {
                handleOnScreenResumed()
            }
            is ParkEvent.OnAddParkClicked -> {
                handleOnAddPark()
            }
            is ParkEvent.OnParkClicked -> {
                _uiEvent.update { it.copy(test = "Park clicked") }
            }
            is ParkEvent.OnDeleteParkClicked -> {
                _uiEvent.update { it.copy(test = "Delete park clicked") }
            }

        }
    }

    private fun handleOnScreenResumed() {
        viewModelScope.launch {
            _uiEvent.update { state ->
                state.copy(parkHistory = repository.getParkHistory())
            }
        }
    }

    private fun handleOnAddPark() {
        viewModelScope.launch {
            repository.insertPark(createMockedPark())

            _uiEvent.update { state ->
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

    object onScreenResumed: ParkEvent()
    object OnAddParkClicked: ParkEvent()
    data class OnParkClicked(val park: Park): ParkEvent()
    data class OnDeleteParkClicked(val park: Park): ParkEvent()
}