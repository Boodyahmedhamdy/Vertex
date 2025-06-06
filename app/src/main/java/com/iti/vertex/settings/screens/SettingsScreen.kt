package com.iti.vertex.settings.screens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.settings.Language
import com.iti.vertex.data.sources.local.settings.LocationProvider
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.data.sources.local.settings.TempUnit
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import com.iti.vertex.navigation.routes.Routes
import com.iti.vertex.settings.vm.SettingsViewModel

private const val TAG = "SettingsScreen"

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val windSpeedState = viewModel.windSpeedUnitState.collectAsStateWithLifecycle()
    val languageState = viewModel.languageState.collectAsStateWithLifecycle()
    val tempUnitState = viewModel.tempUnitState.collectAsStateWithLifecycle()
    val locationProviderState = viewModel.locationProviderState.collectAsStateWithLifecycle()
    val locationState = viewModel.locationState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if(result.values.all { it }) navController.navigate(Routes.LocationPickerScreenRoute)
    }



    SettingsScreenContent(
        windSpeedUnitState = windSpeedState.value,
        onWindSpeedUnitChanged = { viewModel.setWindSpeedUnit(it) },
        languageState = languageState.value,
        onLanguageChanged = { viewModel.setLanguageState(it) },
        tempUnitState = tempUnitState.value,
        onTempUnitChanged = { viewModel.setTempUnit(it) },
        locationProviderState = locationProviderState.value,
        onLocationProviderChanged = {
            viewModel.setLocationProvider(it)
            when(it) {
                LocationProvider.GPS -> {
                    Log.i(TAG, "SettingsScreen: gps selected")
                }
                LocationProvider.MAP -> permissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        },
        locationState = locationState.value,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    windSpeedUnitState: WindSpeedUnit,
    onWindSpeedUnitChanged: (WindSpeedUnit) -> Unit,
    languageState: Language,
    onLanguageChanged: (Language) -> Unit,
    tempUnitState: TempUnit,
    onTempUnitChanged: (TempUnit) -> Unit,
    locationProviderState: LocationProvider,
    onLocationProviderChanged: (LocationProvider) -> Unit,
    locationState: MyLocation,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.settings)) }) },
        ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState())
        ){

            // wind speed card
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                // title
                Text(
                    text = stringResource(R.string.wind_speed_unit),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )

                // options
                FlowRow {
                    WindSpeedUnit.entries.forEach { windSpeedUnit ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onWindSpeedUnitChanged(windSpeedUnit) }
                        ){
                            RadioButton(
                                selected = windSpeedUnitState == windSpeedUnit,
                                onClick = { onWindSpeedUnitChanged(windSpeedUnit) }
                            )
                            Text(text = stringResource(windSpeedUnit.displayName))
                        }
                    }
                }
            }

            // language
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                // title
                Text(
                    text = stringResource(R.string.language),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )

                // options
                FlowRow() {
                    Language.entries.forEach { language ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onLanguageChanged(language) }
                        ){
                            RadioButton(
                                selected = languageState == language,
                                onClick = { onLanguageChanged(language) }
                            )
                            Text(text = stringResource(language.displayName))
                        }
                    }
                }
            }

            // temperature unit card
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                // title
                Text(
                    text = stringResource(R.string.temperature_unit),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )

                // options
                FlowRow {
                    TempUnit.entries.forEach { tempUnit ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onTempUnitChanged(tempUnit) }
                        ){
                            RadioButton(
                                selected = tempUnitState == tempUnit,
                                onClick = { onTempUnitChanged(tempUnit) }
                            )
                            Text(text = stringResource(tempUnit.displayName))
                        }
                    }
                }
            }

            // locationState provider
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                // title
                Text(
                    text = stringResource(R.string.location_provider),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )

                // options
                FlowRow {
                    LocationProvider.entries.forEach { provider ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onLocationProviderChanged(provider) }
                        ){
                            RadioButton(
                                selected = locationProviderState == provider,
                                onClick = { onLocationProviderChanged(provider) }
                            )
                            Text(text = stringResource(provider.displayName))
                        }
                    }
                }
            }

            Text(text = stringResource(
                R.string.current_location_is_latitude_longitude,
                locationState.lat,
                locationState.long
            ), modifier = Modifier.fillMaxWidth().padding(8.dp))

        }
    }


}

