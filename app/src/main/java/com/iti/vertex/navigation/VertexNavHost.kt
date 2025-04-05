package com.iti.vertex.navigation

import android.app.AlarmManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.work.WorkManager
import com.google.android.libraries.places.api.Places
import com.iti.vertex.BuildConfig
import com.iti.vertex.alarms.VertexAlarmManager
import com.iti.vertex.alarms.screens.AlarmsScreen
import com.iti.vertex.data.sources.local.alarms.AlarmsLocalDataSource
import com.iti.vertex.data.repos.alarms.AlarmsRepository
import com.iti.vertex.alarms.vm.AlarmsViewModel
import com.iti.vertex.alarms.vm.AlarmsViewModelFactory
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.repos.settings.SettingsRepository
import com.iti.vertex.data.sources.local.db.DatabaseHelper
import com.iti.vertex.data.sources.local.forecast.ForecastLocalDataSource
import com.iti.vertex.data.sources.local.settings.DataStoreHelper
import com.iti.vertex.data.sources.local.settings.SettingsLocalDataSource
import com.iti.vertex.data.sources.remote.api.RetrofitHelper
import com.iti.vertex.data.sources.remote.forecast.ForecastRemoteDataSource
import com.iti.vertex.details.screens.ForecastDetailsScreen
import com.iti.vertex.details.vm.ForecastDetailsViewModel
import com.iti.vertex.details.vm.ForecastDetailsViewModelFactory
import com.iti.vertex.favorite.screens.FavoritesScreen
import com.iti.vertex.favorite.vm.FavoriteViewModel
import com.iti.vertex.favorite.vm.FavoriteViewModelFactory
import com.iti.vertex.home.screens.HomeScreen
import com.iti.vertex.home.vm.HomeViewModel
import com.iti.vertex.home.vm.HomeViewModelFactory
import com.iti.vertex.locationpicker.screens.LocationPickerScreen
import com.iti.vertex.locationpicker.vm.LocationPickerViewModel
import com.iti.vertex.locationpicker.vm.LocationPickerViewModelFactory
import com.iti.vertex.settings.screens.SettingsScreen
import com.iti.vertex.navigation.routes.Routes
import com.iti.vertex.settings.vm.SettingsViewModel
import com.iti.vertex.settings.vm.SettingsViewModelFactory
import kotlinx.coroutines.Dispatchers

private const val TAG = "VertexNavHost"
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VertexNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current


    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreenRoute,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        modifier = modifier
    ) {
        composable<Routes.HomeScreenRoute> {
            val factory = HomeViewModelFactory(
                repository = ForecastRepository.getInstance(
                    remoteDataSource = ForecastRemoteDataSource(api = RetrofitHelper.apiService, ioDispatcher = Dispatchers.IO),
                    localDataSource = ForecastLocalDataSource(dao = DatabaseHelper.getForecastDao(context = context))
                ),
                settingsRepository = SettingsRepository.getInstance(SettingsLocalDataSource(
                    DataStoreHelper(context)
                ))
            )
            val viewModel: HomeViewModel = viewModel(factory = factory)

            HomeScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize().padding(8.dp),
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
            val viewModel: ForecastDetailsViewModel = viewModel(factory = ForecastDetailsViewModelFactory(repository = ForecastRepository.getInstance(
                remoteDataSource = ForecastRemoteDataSource(api = RetrofitHelper.apiService, ioDispatcher = Dispatchers.IO),
                localDataSource = ForecastLocalDataSource(dao = DatabaseHelper.getForecastDao(context = context))
            ), settingsRepository = SettingsRepository.getInstance(SettingsLocalDataSource(DataStoreHelper(context)))))
            viewModel.load(lat = route.lat, long = route.long)
            ForecastDetailsScreen(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }

        composable<Routes.AlarmsScreenRoute> {

            val viewModel: AlarmsViewModel = viewModel(
                factory = AlarmsViewModelFactory(
                    alarmsRepository = AlarmsRepository.getInstance(AlarmsLocalDataSource(alarmsDao = DatabaseHelper.getAlarmsDao(context))),
                    settingsRepository = SettingsRepository.getInstance(SettingsLocalDataSource(
                        DataStoreHelper(context)
                    )),
                    workManager = WorkManager.getInstance(context)
                )
            )

            AlarmsScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }

        composable<Routes.SettingsScreenRoute> {

            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(
                    repository = SettingsRepository.getInstance(
                        settingsLocalDataSource = SettingsLocalDataSource(dataStoreHelper = DataStoreHelper(context))
                    )
                )
            )
            SettingsScreen(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }

        composable<Routes.LocationPickerScreenRoute> {
            Places.initializeWithNewPlacesApiEnabled(context.applicationContext, BuildConfig.MAPS_API_KEY)
            val viewModel: LocationPickerViewModel = viewModel(
                factory = LocationPickerViewModelFactory(
                    forecastRepository = ForecastRepository.getInstance(
                        remoteDataSource = ForecastRemoteDataSource(RetrofitHelper.apiService, Dispatchers.IO),
                        localDataSource = ForecastLocalDataSource(DatabaseHelper.getForecastDao(context))
                    ),
                    settingsRepository = SettingsRepository.getInstance(SettingsLocalDataSource(DataStoreHelper(context))),
                    placesClient =  Places.createClient(context.applicationContext)
                )
            )
            LocationPickerScreen(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }
    }


}