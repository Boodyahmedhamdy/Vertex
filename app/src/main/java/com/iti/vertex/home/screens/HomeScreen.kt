package com.iti.vertex.home.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.vertex.R
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import com.iti.vertex.details.screens.ForecastSection
import com.iti.vertex.home.components.CurrentWeatherConditionsSection
import com.iti.vertex.home.components.CurrentWeatherSection
import com.iti.vertex.home.states.ForecastUiState
import com.iti.vertex.home.vm.HomeViewModel
import com.iti.vertex.utils.Result
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val currentWeatherState = viewModel.currentWeatherState.collectAsStateWithLifecycle()
    val forecastState = viewModel.forecastState.collectAsStateWithLifecycle()
    val isRefreshingState = viewModel.isRefreshing.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messageSharedFlow.collect {
            scope.launch { snackBarHostState.showSnackbar(message = context.getString(it)) }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.home)) }) },
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshingState.value,
            onRefresh = { viewModel.refresh() },
            modifier = modifier.padding(paddingValues)
        ) {
            HomeScreenContent(
                currentWeatherState = currentWeatherState.value,
                forecastState = forecastState.value,
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenContent(
    currentWeatherState: Result<out CurrentWeatherResponse>,
    forecastState: Result<out ForecastUiState>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        // current weather section
        when(currentWeatherState) {
            Result.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator()
                }
            }
            is Result.Error -> {
                Text(text = currentWeatherState.message, color = MaterialTheme.colorScheme.error)
            }
            is Result.Success -> {
                CurrentWeatherSection(
                    state = currentWeatherState.data.toCurrentWeatherUiState(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                CurrentWeatherConditionsSection(
                    state = currentWeatherState.data.toCurrentWeatherUiState().toConditionsList()
                )
            }
        }

        // forecast section
        when(forecastState) {
            Result.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator()
                }
            }
            is Result.Error -> {
                Text(text = forecastState.message, color = MaterialTheme.colorScheme.error)
            }
            is Result.Success -> {
                ForecastSection(
                    state = forecastState.data.toEntity()
                )
            }
        }
    }
}

