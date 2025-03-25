package com.iti.vertex.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iti.vertex.alarms.screens.AlarmsScreen
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.sources.local.db.DatabaseHelper
import com.iti.vertex.data.sources.local.forecast.ForecastLocalDataSource
import com.iti.vertex.data.sources.remote.api.RetrofitHelper
import com.iti.vertex.data.sources.remote.forecast.ForecastRemoteDataSource
import com.iti.vertex.favorite.generateRandomLatLng
import com.iti.vertex.favorite.screens.FavoritesScreen
import com.iti.vertex.favorite.vm.FavoriteViewModel
import com.iti.vertex.favorite.vm.FavoriteViewModelFactory
import com.iti.vertex.home.screens.HomeScreen
import com.iti.vertex.home.vm.HomeViewModel
import com.iti.vertex.home.vm.HomeViewModelFactory
import com.iti.vertex.locationpicker.screens.LocationPickerScreen
import com.iti.vertex.settings.screens.SettingsScreen
import com.iti.vertex.navigation.routes.Routes
import kotlinx.coroutines.Dispatchers

private const val TAG = "VertexNavHost"
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VertexNavHost(
    navController: NavHostController,
    lat: Double,
    long: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreenRoute,
        modifier = modifier
    ) {
        composable<Routes.HomeScreenRoute> {
            val api = RetrofitHelper.apiService
            val remoteDataSource = ForecastRemoteDataSource(api = api, ioDispatcher = Dispatchers.IO)
            val dao = DatabaseHelper.getForecastDao(context = context)
            val localDataSource = ForecastLocalDataSource(dao = dao)
            val repository = ForecastRepository(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource
            )
            val factory = HomeViewModelFactory(repository = repository)
            val viewModel: HomeViewModel = viewModel(factory = factory)
            viewModel.setLatLong(lat = lat, long = long)
            val state = viewModel.state.collectAsStateWithLifecycle()
            HomeScreen(
                state = state.value,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                onRefresh = {
                    viewModel.refresh()
                }
            )
        }

        composable<Routes.FavoriteScreenRoute> {
            val api = RetrofitHelper.apiService
            val remoteDataSource = ForecastRemoteDataSource(api = api, ioDispatcher = Dispatchers.IO)
            val dao = DatabaseHelper.getForecastDao(context = context)
            val localDataSource = ForecastLocalDataSource(dao = dao)
            val repository = ForecastRepository(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource
            )
            val factory = FavoriteViewModelFactory(forecastRepository = repository)
            val viewModel: FavoriteViewModel = viewModel(factory = factory)
            val state = viewModel.state.collectAsStateWithLifecycle()
            FavoritesScreen(
                state = state.value,
                onItemClicked = { Log.i(TAG, "VertexNavHost: clicked on ${it}") },
                onDeleteItemClicked = {  },
                onInsertFabClicked = {
                    Log.i(TAG, "VertexNavHost: clicked on fab")
                    val random = generateRandomLatLng()
                    viewModel.insertLocationToFavorite(lat = random.first, long = random.second)
                 },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            )
        }

        composable<Routes.AlarmsScreenRoute> {
            AlarmsScreen(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp))
        }

        composable<Routes.SettingsScreenRoute> {
            SettingsScreen(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp))
        }

        composable<Routes.LocationPickerScreenRoute> {
            LocationPickerScreen(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp))
        }
    }


}