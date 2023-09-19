package com.example.segnaposto.feature.base

import com.example.segnaposto.util.CloseableCoroutineScope
import com.example.segnaposto.util.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual abstract class BaseViewModel {
    private var isCleared = false

    actual val viewModelScope: CoroutineScope by lazy(::buildScope)

    private fun buildScope(): CloseableCoroutineScope = CloseableCoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    ).apply {
        if (isCleared) close()
    }

    //protected actual open fun onCleared() {}

    fun clear() {
        isCleared = true
        viewModelScope.close()
       // onCleared()
    }
}