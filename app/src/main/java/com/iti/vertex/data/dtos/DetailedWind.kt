package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName

data class DetailedWind(
    @SerializedName("deg")
    val deg: Int = 0,
    @SerializedName("gust")
    val gust: Double = 0.0,
    @SerializedName("speed")
    val speed: Double = 0.0
)