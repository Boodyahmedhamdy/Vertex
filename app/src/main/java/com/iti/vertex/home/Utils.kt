package com.iti.vertex.home

import android.util.Log
import com.iti.vertex.BuildConfig

private const val TAG = "Utils"
fun String.toWeatherIconUrl(): String {
    Log.i(TAG, "toWeatherIconUrl: ${this}")
    val result = "https://openweathermap.org/img/wn/${this}@2x.png?appid=${BuildConfig.API_KEY}"
    Log.i(TAG, "toWeatherIconUrl: result: $result")
    return result
}
