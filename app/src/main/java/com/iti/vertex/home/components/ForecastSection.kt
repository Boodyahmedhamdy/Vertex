package com.iti.vertex.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.home.states.ForecastUiState

@Composable
fun ForecastSection(
    state: List<SimpleForecastItem>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(state) {
            ForecastSectionListItem(
                state = it,
                modifier = Modifier.padding(8.dp)
            )
        }


    }

}

@Preview
@Composable
private fun ForecastSectionPreview() {

}