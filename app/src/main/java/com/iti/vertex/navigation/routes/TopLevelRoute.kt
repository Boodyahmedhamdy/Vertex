package com.iti.vertex.navigation.routes

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.iti.vertex.R


data class TopLevelRoute<T : Any>(
    @StringRes val name: Int,
    val route: T,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
)

val topLevelRoutes = listOf(
    TopLevelRoute<Routes>(R.string.home, Routes.HomeScreenRoute, Icons.Filled.Home, Icons.Outlined.Home),
    TopLevelRoute<Routes>(R.string.favorites, Routes.FavoriteScreenRoute, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    TopLevelRoute<Routes>(R.string.alarms, Routes.AlarmsScreenRoute, Icons.Filled.Notifications, Icons.Outlined.Notifications),
    TopLevelRoute<Routes>(R.string.settings, Routes.SettingsScreenRoute, Icons.Filled.Settings, Icons.Outlined.Settings),
)