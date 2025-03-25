package com.iti.vertex.data.dtos


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("coord")
    @Embedded
    val coord: Coord = Coord(),
    @SerializedName("country")
    val country: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("population")
    val population: Int = 0,
    @SerializedName("sunrise")
    val sunrise: Int = 0,
    @SerializedName("sunset")
    val sunset: Int = 0,
    @SerializedName("timezone")
    val timezone: Int = 0
)