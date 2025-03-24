package com.iti.vertex.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.home.states.ForecastUiState

@Composable
fun ForecastSectionDay(
    title: String,
    state: List<SimpleForecastItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier =  modifier) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        ForecastSection(
            state = state
        )
    }

}

