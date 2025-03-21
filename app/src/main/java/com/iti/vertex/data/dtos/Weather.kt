package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("main")
    val main: String = ""
)