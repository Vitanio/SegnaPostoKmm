package com.example.segnaposto.domain.viewModel

import com.example.segnaposto.domain.Park

data class ParkState(
    val test: String = "",
    val parkHistory: List<Park> = emptyList()
)