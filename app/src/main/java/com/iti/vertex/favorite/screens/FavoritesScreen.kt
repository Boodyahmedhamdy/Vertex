package com.iti.vertex.favorite.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.favorite.FavoriteScreenUiState
import com.iti.vertex.ui.components.PlaceholderScreen

@Composable
fun FavoritesScreen(
    state: FavoriteScreenUiState,
    onItemClicked: (ForecastEntity) -> Unit,
    onDeleteItemClicked: (ForecastEntity) -> Unit,
    onInsertFabClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onInsertFabClicked,
            ) {
                if(state.isLoading)
                    CircularProgressIndicator()
                else
                Icon(imageVector = Icons.Filled.Place, contentDescription = "add location")
            }
        }
    ){ paddingValues ->
        if(state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            if(state.items.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {
                    Image(
                        painter = painterResource(R.drawable.clouds),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                    )
                    Text(text = "No Favorite Locations yet!!")
                }
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {
                    items(state.items) {
                        FavoriteLocationListItem(
                            state = it,
                            onItemClicked = onItemClicked,
                            onDeleteItemClicked = onDeleteItemClicked,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                    }

                }
            }
        }
    }


}

@Composable
fun FavoriteLocationListItem(
    state: ForecastEntity,
    onItemClicked: (ForecastEntity) -> Unit,
    onDeleteItemClicked: (ForecastEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onItemClicked(state) },
        modifier = modifier
    ) {
        Text(text = "Lat = ${state.city.coord.lat}", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))
        Text(text = "Long = ${state.city.coord.lon}", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))
    }
}
