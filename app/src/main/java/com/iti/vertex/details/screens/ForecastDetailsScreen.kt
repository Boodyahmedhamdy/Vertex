package com.iti.vertex.details.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.details.vm.ForecastDetailsViewModel
import com.iti.vertex.favorite.screens.EmptyScreen
import com.iti.vertex.home.components.ForecastSectionDay
import com.iti.vertex.home.getForecastMap
import com.iti.vertex.utils.Result

@Composable
fun ForecastDetailsScreen(
    viewModel: ForecastDetailsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    ForecastDetailsScreenContent(
        uiState = uiState.value,
        onSetCurrentLocationClicked = { lat, long ->
            viewModel.setCurrentLocation(lat, long)
        },
        modifier = modifier
    )

}

@Composable
fun ForecastDetailsScreenContent(
    uiState: Result<out ForecastEntity>,
    onSetCurrentLocationClicked: (lat: Double, long: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    when(uiState) {
        is Result.Error -> {
            EmptyScreen(
                drawableRes = R.drawable.baseline_broken_image_24,
                message = uiState.message,
                modifier = modifier
            )
        }
        Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        }
        is Result.Success -> {
            Column {
                OutlinedButton( onClick = { onSetCurrentLocationClicked(uiState.data.city.coord.lat, uiState.data.city.coord.lon) } ) {
                    Text(text = stringResource(R.string.set_as_current_location))
                }

                ForecastSection(
                    state = uiState.data,
                    modifier = modifier.verticalScroll(rememberScrollState())
                )
            }
        }
    }
}


@Composable
fun ForecastSection(
    state: ForecastEntity,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // title of the locationState
        Text(
            text = state.city.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.forecast_for_5_days),
            style = MaterialTheme.typography.titleLarge
        )

        for(entry in getForecastMap(state.list)) {
            ForecastSectionDay(title = entry.key, state = entry.value)
        }
    }

}