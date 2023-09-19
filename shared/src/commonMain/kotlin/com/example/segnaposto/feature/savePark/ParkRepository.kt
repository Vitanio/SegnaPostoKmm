package com.example.segnaposto.feature.savePark

import app.cash.sqldelight.db.SqlDriver
import com.example.segnaposto.data.park.SqlDelightParkDataSource
import com.example.segnaposto.database.ParkDatabase
import com.example.segnaposto.feature.savePark.model.Park


class ParkRepository(driver: SqlDriver) {

    init {
    }

    val database =
        SqlDelightParkDataSource(ParkDatabase(driver))

    suspend fun insertPark(park: Park){
        database.insertPark(park)
    }

}