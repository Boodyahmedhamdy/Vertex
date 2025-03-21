package com.iti.vertex.data.dtos.current


import com.google.gson.annotations.SerializedName

data class SimpleWind(
    @SerializedName("deg")
    val deg: Int = 0,
    @SerializedName("speed")
    val speed: Double = 0.0
)