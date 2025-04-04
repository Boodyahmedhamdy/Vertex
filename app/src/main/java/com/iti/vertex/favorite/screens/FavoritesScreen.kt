package com.iti.vertex.favorite.screens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.iti.vertex.R
import com.iti.vertex.favorite.components.FavoriteLocationListItem
import com.iti.vertex.favorite.generateRandomLatLng
import com.iti.vertex.favorite.vm.FavoriteViewModel
import com.iti.vertex.navigation.routes.Routes
import com.iti.vertex.utils.Result
import kotlinx.coroutines.launch

private const val TAG = "FavoritesScreen"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.favoriteScreenState.collectAsStateWithLifecycle().value
    val showDeleteLocationDialogState = viewModel.showDeleteLocationDialog.collectAsStateWithLifecycle()
    val selectedItemToDelete = viewModel.selectedItemToBeDeleted.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    val snackBarState = remember { SnackbarHostState() }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if(result.values.all { it }) navController.navigate(Routes.LocationPickerScreenRoute)
    }

    LaunchedEffect(Unit) {
        viewModel.messageSharedFlow.collect {
            if(it.isNotBlank()) {
                scope.launch { snackBarState.showSnackbar(message = it) }
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.favorites)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                permissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            },) {
                Icon(imageVector = Icons.Filled.Place, contentDescription = "add locationState")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
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
                EmptyScreen(
                    drawableRes = R.drawable.wind,
                    message = uiState.message,
                    modifier = Modifier.fillMaxSize()
                )
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

                    if(showDeleteLocationDialogState.value) {
                        ConfirmationDialog(
                            onDismiss = { viewModel.toggleShowDeleteLocationDialog() },
                            onCancel = { viewModel.toggleShowDeleteLocationDialog() },
                            onConfirm = {
                                viewModel.deleteForecast(selectedItemToDelete)
                                viewModel.toggleShowDeleteLocationDialog()
                            }
                        )
                    }

                    LazyVerticalGrid (
                        columns = GridCells.Adaptive(200.dp),
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(uiState.data, key = {it.city.id}) {
                            FavoriteLocationListItem(
                                state = it,
                                onItemClicked = {
                                    navController.navigate(Routes.ForecastDetailsScreenRoute(
                                        lat = it.city.coord.lat, long = it.city.coord.lon
                                    ))
                                },
                                onDeleteItemClicked = {
                                    viewModel.updateSelectedItemToBeDeleted(it)
                                    viewModel.toggleShowDeleteLocationDialog()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp).animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Are you sure to Delete ?")
                Row {
                    TextButton(onClick = onConfirm) {
                        Text("Delete")
                    }
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                }
            }
        }
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

