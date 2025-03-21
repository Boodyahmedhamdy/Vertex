package com.iti.vertex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iti.vertex.alarms.screens.AlarmsScreen
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.sources.remote.api.RetrofitHelper
import com.iti.vertex.data.sources.remote.forecast.ForecastRemoteDataSource
import com.iti.vertex.favorite.screens.FavoritesScreen
import com.iti.vertex.home.screens.HomeScreen
import com.iti.vertex.home.vm.HomeViewModel
import com.iti.vertex.home.vm.HomeViewModelFactory
import com.iti.vertex.locationpicker.screens.LocationPickerScreen
import com.iti.vertex.settings.screens.SettingsScreen
import com.iti.vertex.ui.navigation.routes.Routes
import kotlinx.coroutines.Dispatchers

@Composable
fun VertexNavHost(
    navController: NavHostController,
    onAddToFavoriteButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreenRoute,
        modifier = modifier
    ) {
        composable<Routes.HomeScreenRoute> {
            val api = RetrofitHelper.apiService
            val remoteDataSource = ForecastRemoteDataSource(api = api, ioDispatcher = Dispatchers.IO)
            val repository = ForecastRepository(remoteDataSource)
            val factory = HomeViewModelFactory(repository)
            val viewModel: HomeViewModel = viewModel(factory = factory)
            val state = viewModel.state.collectAsStateWithLifecycle()
            HomeScreen(
                state = state.value,
                modifier = modifier,
                onRefresh = {}
            )
        }

        composable<Routes.FavoriteScreenRoute> {
            FavoritesScreen(
                modifier = modifier,
                onAddToFavoriteButtonClicked = onAddToFavoriteButtonClicked
            )
        }

        composable<Routes.AlarmsScreenRoute> {
            AlarmsScreen(modifier = modifier)
        }

        composable<Routes.SettingsScreenRoute> {
            SettingsScreen(modifier = modifier)
        }

        composable<Routes.LocationPickerScreenRoute> {
            LocationPickerScreen(modifier = modifier)
        }
    }


}