package com.iti.vertex.utils

import com.iti.vertex.R

fun getStringResFromConditionCode(code: Int): Int {
    return when (code) {
        200 -> R.string.thunderstorm_light_rain
        201 -> R.string.thunderstorm_rain
        202 -> R.string.thunderstorm_heavy_rain
        210 -> R.string.light_thunderstorm
        211 -> R.string.thunderstorm
        212 -> R.string.heavy_thunderstorm
        221 -> R.string.ragged_thunderstorm
        230 -> R.string.thunderstorm_light_drizzle
        231 -> R.string.thunderstorm_drizzle
        232 -> R.string.thunderstorm_heavy_drizzle

        300 -> R.string.light_intensity_drizzle
        301 -> R.string.drizzle
        302 -> R.string.heavy_intensity_drizzle
        310 -> R.string.light_intensity_drizzle_rain
        311 -> R.string.drizzle_rain
        312 -> R.string.heavy_intensity_drizzle_rain
        313 -> R.string.shower_rain_and_drizzle
        314 -> R.string.heavy_shower_rain_and_drizzle
        321 -> R.string.shower_drizzle

        500 -> R.string.light_rain
        501 -> R.string.moderate_rain
        502 -> R.string.heavy_intensity_rain
        503 -> R.string.very_heavy_rain
        504 -> R.string.extreme_rain
        511 -> R.string.freezing_rain
        520 -> R.string.light_intensity_shower_rain
        521 -> R.string.shower_rain
        522 -> R.string.heavy_intensity_shower_rain
        531 -> R.string.ragged_shower_rain

        600 -> R.string.snow_condition_600
        601 -> R.string.snow_condition_601
        602 -> R.string.snow_condition_602
        611 -> R.string.snow_condition_611
        612 -> R.string.snow_condition_612
        613 -> R.string.snow_condition_613
        615 -> R.string.snow_condition_615
        616 -> R.string.snow_condition_616
        620 -> R.string.snow_condition_620
        621 -> R.string.snow_condition_621
        622 -> R.string.snow_condition_622

        701 -> R.string.atmosphere_condition_701
        711 -> R.string.atmosphere_condition_711
        721 -> R.string.atmosphere_condition_721
        731 -> R.string.atmosphere_condition_731
        741 -> R.string.atmosphere_condition_741
        751 -> R.string.atmosphere_condition_751
        761 -> R.string.atmosphere_condition_761
        762 -> R.string.atmosphere_condition_762
        771 -> R.string.atmosphere_condition_771
        781 -> R.string.atmosphere_condition_781

        800 -> R.string.weather_condition_800
        801 -> R.string.weather_condition_801
        802 -> R.string.weather_condition_802
        803 -> R.string.weather_condition_803
        804 -> R.string.weather_condition_804

        else -> R.string.app_name  // Default case
    }
}