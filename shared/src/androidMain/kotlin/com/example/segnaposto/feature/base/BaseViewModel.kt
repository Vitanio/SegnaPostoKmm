package com.example.segnaposto.feature.base

import androidx.lifecycle.ViewModel as AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.viewModelScope as superViewModelScope

actual abstract class BaseViewModel : AndroidViewModel() {
    actual val viewModelScope: CoroutineScope = superViewModelScope
    //actual override fun onCleared() {}
}