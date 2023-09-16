package com.example.segnaposto.domain
data class Park(
    val id: Long? = null,
    val title: String,
    val description: String?,
    val number: String?,
    val latitude: Double,
    val longitude: Double,
    val date: String
) {
}