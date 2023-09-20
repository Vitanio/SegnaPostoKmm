package com.example.segnaposto.feature.savePark.model
data class Park(
    val id: String = "",
    val title: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val date: String
) {
}