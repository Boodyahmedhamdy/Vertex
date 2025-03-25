package com.iti.vertex.favorite

import kotlin.random.Random

fun generateRandomLatLng(): Pair<Double, Double> {
    val latitude = Random.nextDouble(-90.0, 90.0)  // Latitude ranges from -90 to 90
    val longitude = Random.nextDouble(-180.0, 180.0)  // Longitude ranges from -180 to 180
    return Pair(latitude, longitude)
}