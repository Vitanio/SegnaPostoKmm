package com.example.segnaposto.di

import app.cash.sqldelight.db.SqlDriver
import com.example.segnaposto.data.local.DatabaseDriverFactory

class DatabaseModule {

    private val factory by lazy { DatabaseDriverFactory() }

    val driver: SqlDriver by lazy {
        factory.createDriver()
    }

    /*
    val parkDataSource: ParkDataSource by lazy {
        SqlDelightParkDataSource(ParkDatabase(factory.createDriver()))
    }

     */
}