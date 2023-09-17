package com.example.segnaposto.domain.viewModel

import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel() {
    val viewModelScope: CoroutineScope
    // protected actual open fun onCleared()
}