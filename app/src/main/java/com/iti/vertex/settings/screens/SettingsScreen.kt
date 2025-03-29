package com.iti.vertex.settings.screens

import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.settings.Language
import com.iti.vertex.data.sources.local.settings.LocationProvider
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
                LocationProvider.MAP -> navController.navigate(Routes.LocationPickerScreenRoute)
            }
        },
        modifier = modifier.verticalScroll(rememberScrollState()),
    )


}

@OptIn(ExperimentalLayoutApi::class)
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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

        // location provider
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

    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {

}