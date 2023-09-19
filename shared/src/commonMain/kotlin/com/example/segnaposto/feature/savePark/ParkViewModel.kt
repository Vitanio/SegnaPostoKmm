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

    private val _state = MutableStateFlow(ParkState())
    val state = _state.asStateFlow()

    init {
    }

    fun onParkClicked() {

        viewModelScope.launch {
            repository.insertPark(createMockedPark())
        }

        _state.update { it.copy(test = "Update received from shared " + Random.nextInt(10)) }
    }

    fun createMockedPark(): Park {
        return Park(
            title = "Mola di Bari",
            description = "Via Foggia 78",
            latitude = 10.0,
            longitude = 12.0,
            date = "Domenica"
        )
    }
}