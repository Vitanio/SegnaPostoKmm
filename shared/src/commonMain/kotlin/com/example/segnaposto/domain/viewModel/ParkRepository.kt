package com.example.segnaposto.domain.viewModel

import app.cash.sqldelight.db.SqlDriver
import com.example.segnaposto.data.park.SqlDelightParkDataSource
import com.example.segnaposto.database.ParkDatabase
import com.example.segnaposto.domain.Park


class ParkRepository(driver: SqlDriver) {

    init {
    }

    val database =
        SqlDelightParkDataSource(ParkDatabase(driver))

    suspend fun insertPark(park: Park){
        database.insertPark(park)
    }

}