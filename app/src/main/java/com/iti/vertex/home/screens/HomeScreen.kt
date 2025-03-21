package com.iti.vertex.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import com.iti.vertex.ui.components.PlaceholderScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "HomeScreen",
        onPrimaryButtonClicked = { TODO() },
        onSecondaryButtonClicked = { TODO() },
        modifier = modifier
    )

}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}