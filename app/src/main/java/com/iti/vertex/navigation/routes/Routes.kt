package com.iti.vertex.navigation.routes

import androidx.annotation.StringRes
import com.iti.vertex.R
import kotlinx.serialization.Serializable

@Serializable
/*sealed*/ open class Routes(@StringRes val title: Int = R.string.home) {
    @Serializable
    data object HomeScreenRoute : Routes(R.string.home)

    @Serializable
    data object FavoriteScreenRoute : Routes(R.string.favorites)

    @Serializable
    object AlarmsScreenRoute : Routes(R.string.alarms)

    @Serializable
    object SettingsScreenRoute: Routes(R.string.settings)

    @Serializable
    object LocationPickerScreenRoute: Routes(R.string.location_picker)

    @Serializable
    data class ForecastDetailsScreenRoute(val lat: Double, val long: Double): Routes(R.string.forecast_details)

    @Serializable
    data object FavoriteGraph: Routes()
}