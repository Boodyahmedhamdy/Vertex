package com.iti.vertex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.iti.vertex.navigation.routes.Routes
import kotlinx.coroutines.Dispatchers

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VertexNavHost(
    navController: NavHostController,
    onAddToFavoriteButtonClicked: () -> Unit,
    lat: Double,
    long: Double,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreenRoute,
        modifier = modifier.background(color = Color.Green)
    ) {
        composable<Routes.HomeScreenRoute> {
            val api = RetrofitHelper.apiService
            val remoteDataSource = ForecastRemoteDataSource(api = api, ioDispatcher = Dispatchers.IO)
            val repository = ForecastRepository(remoteDataSource)
            val factory = HomeViewModelFactory(repository)
            val viewModel: HomeViewModel = viewModel(factory = factory)
            viewModel.setLatLong(lat = lat, long = long)
            val state = viewModel.state.collectAsStateWithLifecycle()
            HomeScreen(
                state = state.value,
                modifier = modifier.padding(8.dp).background(Color.Red),
                onRefresh = {
                    viewModel.refresh()
                }
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