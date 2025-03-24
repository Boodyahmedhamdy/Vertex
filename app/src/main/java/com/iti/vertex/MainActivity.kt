package com.iti.vertex

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.iti.vertex.navigation.VertexNavHost
import com.iti.vertex.navigation.routes.Routes
import com.iti.vertex.navigation.routes.topLevelRoutes
import com.iti.vertex.ui.theme.VertexTheme

private const val TAG = "MainActivity"
const val LOCATION_PERMISSIONS_REQUEST_CODE = 12

class MainActivity : ComponentActivity() {
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var showDialog: MutableState<Boolean> = mutableStateOf(false)
    private var showOpenSettingsDialog: MutableState<Boolean> = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(LocationManager::class.java)
        locationClient = LocationServices.getFusedLocationProviderClient(this)


        setContent {
            var lat by remember { mutableDoubleStateOf(0.0) }
            var long by remember { mutableDoubleStateOf(0.0) }
            val showDialogState by remember { showDialog }
            /**
             * check if the permissions are granted
             * if granted -> get last location and update values
             * if not granted -> request permissions from user
             * if user accepted -> return to app successfully
             * if user refused -> show dialog to inform him that the app can't work without location permissions
             * */

            val permissionsGranted = isLocationPermissionsGranted()
            if(!permissionsGranted) {
                showDialog.value = true
            } else { // everything is setup
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationClient.lastLocation.addOnSuccessListener {
                        Log.i(TAG, "onCreate: location is $it before let")
                        it?.let {
                            lat = it.latitude
                            long = it.longitude
                            showDialog.value = false
                            Log.i(TAG, "onCreate: location is lat: $lat, long:$long")
                        }
                    }
                } else {
                    showOpenSettingsDialog.value = true
                    openLocationSettings()
                }

            }


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
                    when {
                        (showDialog.value) -> {
                            PermissionDialog(
                                onDismissRequest = { showDialog.value = false },
                                onConfirmRequest = {
                                    Log.i(TAG, "onCreate: request permissions clicked")
                                    requestLocationPermissions()
                                }
                            )
                        }

                        (!showDialog.value) -> VertexNavHost(
                            navController = navController,
                            onAddToFavoriteButtonClicked = {navController.navigate(Routes.LocationPickerScreenRoute)},
                            lat = lat, long = long,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }

    }

    private fun openLocationSettings() {
        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).also { startActivity(it) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PermissionDialog(
        modifier: Modifier = Modifier,
        onDismissRequest: () -> Unit,
        onConfirmRequest: () -> Unit
    ) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest /*{ showDialog.value = false }*/,
            modifier = modifier
        ) {
            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Important Note", style = MaterialTheme.typography.titleLarge)

                    Text(text = "Vertex needs location permissions to work correctly")

                    Row {
                        TextButton (
                            onClick = onDismissRequest
                        ) { Text(text = "Dismiss") }

                        TextButton(
                            onClick = onConfirmRequest
                        ) { Text(text = "Allow Permissions") }
                    }

                }
            }
        }
    }

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
            Log.i(TAG, "isLocationPermissionsGranted: Refused")
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
