package com.iti.vertex.favorite.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.vertex.ui.components.PlaceholderScreen

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier, onAddToFavoriteButtonClicked: () -> Unit) {
    PlaceholderScreen(
        title = "FavoriteScreen",
        onPrimaryButtonClicked = { onAddToFavoriteButtonClicked() },
        onSecondaryButtonClicked = { TODO() },
        modifier = modifier
    )
}