package com.iti.vertex.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.MainData
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.dtos.Weather
import com.iti.vertex.home.states.HomeScreenUiState
import com.iti.vertex.ui.components.PlaceholderScreen
import com.iti.vertex.ui.theme.VertexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "${state.forecast}",
        onPrimaryButtonClicked = { },
        onSecondaryButtonClicked = { },
        modifier = modifier
    )

    /*PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onRefresh() },
        modifier = modifier
    ) {



    }*/

    /*Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // weather condition string
        Text(
            text = state.forecast.list.first().weather.first().main,
        )

        // temperature
        Text(text = "${state.forecast.list.first().mainData.temp.toInt()}", style = MaterialTheme.typography.displayLarge)

        // time and date
        Text(
            text = "${state.forecast.list.first().dtTxt}"
        )


    }*/

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    VertexTheme {
        HomeScreen(
            state = HomeScreenUiState(
                forecast = FullForecastResponse(
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

                    )
            ),
            onRefresh = {  },
            modifier = Modifier.fillMaxSize()
        )
    }

}