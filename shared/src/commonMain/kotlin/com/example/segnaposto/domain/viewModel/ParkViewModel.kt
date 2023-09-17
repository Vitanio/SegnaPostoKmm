package com.example.segnaposto.domain.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ParkViewModel: ViewModel() {

    private val _state = MutableStateFlow(ParkState())
    val state = _state.asStateFlow()

    init {
    }

    fun onParkClicked() {
        _state.update { it.copy(test = "Update received from shared " + Random.nextInt(10)) }
    }
}