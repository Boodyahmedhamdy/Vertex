package com.iti.vertex.favorite.screens

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iti.vertex.R
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.favorite.components.FavoriteLocationListItem
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

private const val TAG = "FavoritesScreen"

@Composable
fun FavoritesScreen(
    uiState: Result<out List<ForecastEntity>>,
    message: SharedFlow<String>,
    onItemClicked: (ForecastEntity) -> Unit,
    onDeleteItemClicked: (ForecastEntity) -> Unit,
    onInsertFabClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val snackBarState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        message.collect {
            if(it.isNotBlank()) {
                scope.launch { snackBarState.showSnackbar(message = it) }
            }
        }
    }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onInsertFabClicked,) {
                Icon(imageVector = Icons.Filled.Place, contentDescription = "add location")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarState) }
    ){ paddingValues ->

        when(uiState) {
            Result.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            }

            is Result.Error -> {
                Log.i(TAG, "FavoritesScreen: error happened ${uiState.message}")
                // what to do here
            }

            is Result.Success -> {
                if(uiState.data.isEmpty()) {
                    EmptyScreen(
                        drawableRes = R.drawable.clouds,
                        message = "No Favorite Locations yet!!",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                } else {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(uiState.data) {
                            FavoriteLocationListItem(
                                state = it,
                                onItemClicked = onItemClicked,
                                onDeleteItemClicked = onDeleteItemClicked,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        /*if(uiState.isLoading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            if(uiState.items.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(uiState.items) {
                        FavoriteLocationListItem(
                            favoriteScreenState = it,
                            onItemClicked = onItemClicked,
                            onDeleteItemClicked = onDeleteItemClicked,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }

                }
            }
        }*/
    }
}


@Composable
fun EmptyScreen(
    @DrawableRes drawableRes: Int,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
        Text(text = message)
    }
}

