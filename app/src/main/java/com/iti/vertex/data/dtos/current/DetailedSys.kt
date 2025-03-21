package com.iti.vertex.data.dtos.current


import com.google.gson.annotations.SerializedName

data class DetailedSys(
    @SerializedName("country")
    val country: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("sunrise")
    val sunrise: Int = 0,
    @SerializedName("sunset")
    val sunset: Int = 0,
    @SerializedName("type")
    val type: Int = 0
)