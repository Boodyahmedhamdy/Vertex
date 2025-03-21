package com.iti.vertex.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import com.iti.vertex.home.states.HomeScreenUiState
import com.iti.vertex.ui.components.PlaceholderScreen

@Composable
fun HomeScreen(
    state: HomeScreenUiState,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = state.title,
        onPrimaryButtonClicked = { },
        onSecondaryButtonClicked = { },
        modifier = modifier
    )

}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        state = HomeScreenUiState()
    )
}