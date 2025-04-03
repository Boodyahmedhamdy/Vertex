package com.iti.vertex

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.iti.vertex.data.repos.settings.SettingsRepository
import com.iti.vertex.data.sources.local.settings.DataStoreHelper
import com.iti.vertex.data.sources.local.settings.LocationProvider
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.data.sources.local.settings.SettingsLocalDataSource
import com.iti.vertex.navigation.VertexNavHost
import com.iti.vertex.navigation.routes.topLevelRoutes
import com.iti.vertex.ui.components.PermissionDialog
import com.iti.vertex.ui.theme.VertexTheme
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
const val LOCATION_PERMISSIONS_REQUEST_CODE = 12

class MainActivity : /*ComponentActivity*/ AppCompatActivity() {
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var showDialog: MutableState<Boolean> = mutableStateOf(false)
    private lateinit var settingsRepository: SettingsRepository


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(LocationManager::class.java)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        settingsRepository = SettingsRepository.getInstance(SettingsLocalDataSource(DataStoreHelper(this)))

        setContent {
            var showDialogState by remember { showDialog }
            val navController = rememberNavController()
            val backStackEntry by  navController.currentBackStackEntryAsState()

            VertexTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentDestination = backStackEntry?.destination
                            topLevelRoutes.forEach { topLevelRoute ->
                                val isSelected = currentDestination?.hierarchy?.any {
                                    it.hasRoute(topLevelRoute.route::class)
                                } ?: true
                                NavigationBarItem(
                                    icon = { Icon(imageVector = if(isSelected) topLevelRoute.selectedIcon else topLevelRoute.unSelectedIcon, contentDescription = null) },

                                    selected = isSelected,

                                    label = { Text(text = getString(topLevelRoute.name)) },

                                    onClick = {
                                        navController.navigate(topLevelRoute.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },

                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    if(showDialogState) {
                        PermissionDialog(
                            onDismissRequest = { showDialog.value = false },
                            onConfirmRequest = { requestLocationPermissions() }
                        )
                    } else {
                        VertexNavHost(
                            navController = navController,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun handleMapProvider() {
        Log.i(TAG, "handleMapProvider: current location provider is MAP")
    }


    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
        lifecycleScope.launch {
            settingsRepository.getCurrentLocationProvider().collect {currentLocationProvider ->
                when(currentLocationProvider) {
                    LocationProvider.GPS -> handleGpsProvider()
                    LocationProvider.MAP -> handleMapProvider()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleGpsProvider() {
        if(isLocationPermissionsGranted()) { // permissions are okay
            if(isGpsEnabled()) { // gps is on
                locationClient.lastLocation.addOnSuccessListener {
                    it?.let {
                        Log.i(TAG, "handleGpsProvider: current location is lat=${it.latitude}, long=${it.longitude}")
                        val myLocation = MyLocation(lat = it.latitude, long = it.longitude, cityName = "")
                        lifecycleScope.launch { settingsRepository.setCurrentLocation(myLocation) }
                    }
                }
            } else { // gps is off
                openLocationSettings()
            }
        } else { // permissions are not granted
            requestLocationPermissions()
        }
    }

    private fun isGpsEnabled(): Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    private fun openLocationSettings() { Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).also { startActivity(it) } }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        when(requestCode) {
            LOCATION_PERMISSIONS_REQUEST_CODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) { // user accepted everything
                    Log.i(TAG, "onRequestPermissionsResult: user accepted everything")
                    showDialog.value = false
                } else {
                    Log.e(TAG, "onRequestPermissionsResult: user denied the permissions")
                    showDialog.value = true
                }
            }
            else -> { Log.e(TAG, "onRequestPermissionsResult: unhandled permission request") }
        }
    }

    private fun isLocationPermissionsGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "isLocationPermissionsGranted: granted")
            return true
        } else {
            Log.i(TAG, "isLocationPermissionsGranted: denied")
            return false
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSIONS_REQUEST_CODE
        )
    }
}
