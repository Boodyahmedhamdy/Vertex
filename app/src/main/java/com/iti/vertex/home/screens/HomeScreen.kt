package com.iti.vertex.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.MainData
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.dtos.Weather
import com.iti.vertex.home.components.CurrentWeatherConditionsSection
import com.iti.vertex.home.components.CurrentWeatherSection
import com.iti.vertex.home.states.HomeScreenUiState
import com.iti.vertex.ui.theme.VertexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onRefresh() },
        modifier = modifier
    ) {
        if(state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier.verticalScroll(rememberScrollState()).padding(horizontal = 8.dp).fillMaxSize()
            ) {
                CurrentWeatherSection(
                    state = state.currentWeatherUiState,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                CurrentWeatherConditionsSection(
                    state = state.currentWeatherUiState.toConditionsList()
                )

            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    VertexTheme {
        HomeScreen(
            state = HomeScreenUiState(
                forecastUiState = FullForecastResponse(
                    list = listOf(
                        SimpleForecastItem(
                            mainData = MainData(
                                temp = 24.0
                            ),
                            weather = listOf(
                                Weather(
                                    main = "Sunny"
                                )
                            ),
                            dtTxt = "2025-03-21 15:00:00"
                        ),
                    ),

                    ).toUiState()
            ),
            onRefresh = {  },
            modifier = Modifier.fillMaxSize()
        )
    }

}