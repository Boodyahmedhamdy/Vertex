package com.iti.vertex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iti.vertex.alarms.screens.AlarmsScreen
import com.iti.vertex.favorite.screens.FavoritesScreen
import com.iti.vertex.home.screens.HomeScreen
import com.iti.vertex.settings.screens.SettingsScreen
import com.iti.vertex.ui.navigation.routes.Routes

@Composable
fun VertexNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreenRoute,
        modifier = modifier
    ) {
        composable<Routes.HomeScreenRoute> {
            HomeScreen(modifier = modifier)
        }

        composable<Routes.FavoriteScreenRoute> {
            FavoritesScreen(modifier = modifier)
        }

        composable<Routes.AlarmsScreenRoute> {
            AlarmsScreen(modifier = modifier)
        }

        composable<Routes.SettingsScreenRoute> {
            SettingsScreen(modifier = modifier)
        }
    }


}