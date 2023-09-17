package com.example.segnaposto.domain.viewModel

import com.example.segnaposto.data.local.DatabaseDriverFactory
import com.example.segnaposto.data.park.SqlDelightParkDataSource
import com.example.segnaposto.database.ParkDatabase
import com.example.segnaposto.domain.Park
import com.example.segnaposto.domain.ParkDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class ParkViewModel(val repository: ParkRepository): ViewModel() {

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