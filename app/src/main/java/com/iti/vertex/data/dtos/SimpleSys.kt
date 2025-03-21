package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName

data class SimpleSys(
    @SerializedName("pod")
    val pod: String = ""
)