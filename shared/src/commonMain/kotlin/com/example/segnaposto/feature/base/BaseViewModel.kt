package com.example.segnaposto.feature.base

import kotlinx.coroutines.CoroutineScope

expect abstract class BaseViewModel() {
    val viewModelScope: CoroutineScope
    // protected actual open fun onCleared()
}