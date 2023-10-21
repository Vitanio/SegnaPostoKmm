package com.example.segnaposto.feature.savePark.data

import com.example.segnaposto.feature.savePark.model.Park

interface ParkDataSource {
    suspend fun insertPark(id: Long?, park: Park)
    suspend fun getParkById(id: Long): Park?
    suspend fun getAllParks(): List<Park>
    suspend fun deleteParkById(id: Long)
}