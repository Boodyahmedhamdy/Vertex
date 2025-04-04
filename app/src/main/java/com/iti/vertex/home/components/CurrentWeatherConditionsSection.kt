package com.iti.vertex.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.vertex.home.states.SimpleCardConditionItemUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CurrentWeatherConditionsSection(
    state: List<SimpleCardConditionItemUiState>,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier.padding(8.dp)
    ) {
        FlowRow (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            state.forEach {
                SimpleWeatherConditionItemCard(
                    state = it
                )
            }
        }

    }

}