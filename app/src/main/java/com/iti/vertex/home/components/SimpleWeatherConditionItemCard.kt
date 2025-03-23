package com.iti.vertex.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iti.vertex.home.states.SimpleCardConditionItemUiState

@Composable
fun SimpleWeatherConditionItemCard(
    state: SimpleCardConditionItemUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.padding(8.dp).border(width = 2.dp, color = Color.Black)
    ) {
        Image(
            painter = painterResource(id = state.imgResId),
            contentDescription = state.value,
            modifier = Modifier.size(50.dp).align(Alignment.CenterHorizontally).padding(4.dp)
        )

        Text(
            text = "${state.value} ${context.getString(state.unit)}",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp)
        )

        Text(
            text = context.getString(state.label),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp)
        )
    }


}