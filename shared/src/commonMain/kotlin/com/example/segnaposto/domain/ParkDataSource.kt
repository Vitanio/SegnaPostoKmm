package com.example.segnaposto.domain

interface ParkDataSource {
    suspend fun insertPark(park: Park)
    suspend fun getParkById(id: Long): Park?
    suspend fun getAllParks(): List<Park>
    suspend fun deleteParkById(id: Long)
}