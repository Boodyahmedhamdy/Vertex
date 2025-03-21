package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String = ""
)