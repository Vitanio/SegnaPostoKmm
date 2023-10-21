package com.example.segnaposto.feature.savePark

import app.cash.sqldelight.db.SqlDriver
import com.example.segnaposto.data.park.SqlDelightParkDataSource
import com.example.segnaposto.database.ParkDatabase
import com.example.segnaposto.feature.savePark.model.Park


class ParkRepository(driver: SqlDriver) {

    val database =
        SqlDelightParkDataSource(ParkDatabase(driver))

    suspend fun insertPark(id: Long?, park: Park) {
        database.insertPark(id, park)
    }

    suspend fun getParkHistory(): List<Park> {
        return database.getAllParks()
    }

    suspend fun deleteParkById(id: Long) {
        database.deleteParkById(id)
    }

    suspend fun getLatestPark(): Park {
        return getParkHistory().last()
    }

}