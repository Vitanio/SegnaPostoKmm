package com.example.segnaposto.data.park

import com.example.segnaposto.database.ParkDatabase
import com.example.segnaposto.feature.savePark.model.Park
import com.example.segnaposto.feature.savePark.data.ParkDataSource

class SqlDelightParkDataSource(db: ParkDatabase): ParkDataSource {

    private val queries = db.parkQueries

    override suspend fun insertPark(park: Park) {
        queries.insertPark(
            id = park.id,
            title = park.title,
            description = park.description,
            latitude = park.latitude,
            longitude = park.longitude,
            date = park.date
        )
    }

    override suspend fun getParkById(id: Long): Park? {
        return queries
            .getParkById(id)
            .executeAsOneOrNull()
            ?.toPark()
    }

    override suspend fun getAllParks(): List<Park> {
        return queries
            .getAllParks()
            .executeAsList()
            .map { it.toPark() }
    }

    override suspend fun deleteParkById(id: Long) {
        queries.deleteParkById(id)
    }
}