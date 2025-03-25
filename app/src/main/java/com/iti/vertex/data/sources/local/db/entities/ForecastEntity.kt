package com.iti.vertex.data.sources.local.db.entities


import androidx.room.Embedded
import androidx.room.Entity
import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.SimpleForecastItem

@Entity(primaryKeys = arrayOf("lat", "lon"))
data class ForecastEntity(
    @Embedded
    val city: City = City(),

    val list: List<SimpleForecastItem> = listOf(),
)
