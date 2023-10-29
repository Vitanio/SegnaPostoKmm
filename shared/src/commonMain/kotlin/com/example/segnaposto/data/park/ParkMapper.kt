package com.example.segnaposto.data.park

import com.example.segnaposto.feature.savePark.model.Park
import database.ParkEntity

fun ParkEntity.toPark(): Park {
    return Park(
        id = id.toString(),
        title = title,
        description = description,
        number = number,
        latitude = latitude,
        longitude = longitude,
        date = " "
    )
}