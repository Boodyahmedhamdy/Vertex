package com.iti.vertex.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.iti.vertex.alarms.screens.AlarmsScreen
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.sources.local.db.DatabaseHelper
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.forecast.ForecastLocalDataSource
import com.iti.vertex.data.sources.remote.api.RetrofitHelper
import com.iti.vertex.data.sources.remote.forecast.ForecastRemoteDataSource
import com.iti.vertex.details.screens.ForecastDetailsScreen
import com.iti.vertex.details.vm.ForecastDetailsViewModel
import com.iti.vertex.details.vm.ForecastDetailsViewModelFactory
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
import okhttp3.Route

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
            val factory = HomeViewModelFactory(
                repository = ForecastRepository.getInstance(
                    remoteDataSource = ForecastRemoteDataSource(api = RetrofitHelper.apiService, ioDispatcher = Dispatchers.IO),
                    localDataSource = ForecastLocalDataSource(dao = DatabaseHelper.getForecastDao(context = context))
                )
            )
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
            val factory = FavoriteViewModelFactory(forecastRepository = ForecastRepository.getInstance(
                remoteDataSource = ForecastRemoteDataSource(api = RetrofitHelper.apiService, ioDispatcher = Dispatchers.IO),
                localDataSource = ForecastLocalDataSource(dao = DatabaseHelper.getForecastDao(context = context))
            ))
            val viewModel: FavoriteViewModel = viewModel(factory = factory)
            FavoritesScreen(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.fillMaxSize().padding(8.dp),
            )
        }

        composable<Routes.ForecastDetailsScreenRoute> {
            val route = it.toRoute<Routes.ForecastDetailsScreenRoute>()
            val factory = ForecastDetailsViewModelFactory(repository = ForecastRepository.getInstance(
                remoteDataSource = ForecastRemoteDataSource(api = RetrofitHelper.apiService, ioDispatcher = Dispatchers.IO),
                localDataSource = ForecastLocalDataSource(dao = DatabaseHelper.getForecastDao(context = context))
            ))
            val viewModel: ForecastDetailsViewModel = viewModel(factory = factory)
            viewModel.load(lat = route.lat, long = route.long)
            ForecastDetailsScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize().padding(8.dp)
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