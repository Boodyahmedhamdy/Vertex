package com.iti.vertex.ui.navigation.routes

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.iti.vertex.R


data class TopLevelRoute<T : Any>(
    @StringRes val name: Int,
    val route: T,
    val icon: ImageVector
)

val topLevelRoutes = listOf(
    TopLevelRoute<Routes>(R.string.home, Routes.HomeScreenRoute, Icons.Filled.Home),
    TopLevelRoute<Routes>(R.string.favorites, Routes.FavoriteScreenRoute, Icons.Filled.Favorite),
    TopLevelRoute<Routes>(R.string.alarms, Routes.AlarmsScreenRoute, Icons.Filled.Notifications),
    TopLevelRoute<Routes>(R.string.settings, Routes.SettingsScreenRoute, Icons.Filled.Settings),
)