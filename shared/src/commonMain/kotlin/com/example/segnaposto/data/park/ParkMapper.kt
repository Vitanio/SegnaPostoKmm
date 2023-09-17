package com.example.segnaposto.data.park

import com.example.segnaposto.domain.Park
import database.ParkEntity

fun ParkEntity.toPark(): Park {
    return Park(
        id = id,
        title = title,
        description = description,
        latitude = latitude,
        longitude = longitude,
        date = " "
    )
}