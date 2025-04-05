package com.iti.vertex.details.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.details.vm.ForecastDetailsViewModel
import com.iti.vertex.favorite.screens.EmptyScreen
import com.iti.vertex.home.components.ForecastSectionDay
import com.iti.vertex.home.getForecastMap
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun ForecastDetailsScreen(
    viewModel: ForecastDetailsViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.messageSharedFlow.collect {value ->
            snackBarHostState.showSnackbar(message = context.getString(value))
        }
    }


    ForecastDetailsScreenContent(
        uiState = uiState.value,
        onSetCurrentLocationClicked = {
            viewModel.setCurrentLocation(it)
        },
        onClickBack = { navController.popBackStack() },
        snackBarHostState = snackBarHostState,
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastDetailsScreenContent(
    uiState: Result<out ForecastEntity>,
    onSetCurrentLocationClicked: (myLocation: MyLocation) -> Unit,
    onClickBack: () -> Unit,
    snackBarHostState: SnackbarHostState,
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
            Scaffold(
                modifier = modifier,
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.location_forecast_details)) },
                        navigationIcon = { IconButton(onClickBack) { Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = stringResource(R.string.back))} },
                        actions = {
                            IconButton( onClick = {
                                val myLocation = MyLocation(lat = uiState.data.city.coord.lat, long = uiState.data.city.coord.lon, cityName = uiState.data.city.name)
                                onSetCurrentLocationClicked(myLocation)
                            }) {
                                Icon(imageVector = Icons.Filled.LocationOn, contentDescription = stringResource(R.string.set_as_current_location))
                            }
                        }
                    )
                }
            ) { innerPadding ->
                ForecastSection(
                    state = uiState.data,
                    modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState())
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