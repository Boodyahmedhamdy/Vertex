package com.iti.vertex.home

import android.util.Log
import com.iti.vertex.data.dtos.SimpleForecastItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val TAG = "Utils"
fun String.toWeatherIconUrl(): String {
    Log.i(TAG, "toWeatherIconUrl: ${this}")
    val result = "https://openweathermap.org/img/wn/$this@2x.png"
    Log.i(TAG, "toWeatherIconUrl: result: $result")
    return result
}

fun getForecastMap(list: List<SimpleForecastItem>): Map<String, List<SimpleForecastItem>> {
    val map = list.groupBy {
        val day = it.dtTxt.split(" ").first()
        day
    }.mapKeys {
        Log.i(TAG, "getForecastMap: it.key = ${it.key}")
        getDayInfo(it.key)
    }
    Log.i(TAG, "getForecastMap: ${map.entries}")
    Log.i(TAG, "getForecastMap: ${map.keys}")
    Log.i(TAG, "getForecastMap: ${map.size}")


    return map
}

fun getDayInfo(dateStr: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val str = if(dateStr.isBlank()) "2025-2-23" else dateStr
    val givenDate = dateFormat.parse(str) ?: return "Invalid date format"

    val calendar = Calendar.getInstance()
    val today = dateFormat.format(calendar.time)

    return if (dateStr == today) {
        "Today"
    } else {
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        dayOfWeekFormat.format(givenDate)
    }
}
